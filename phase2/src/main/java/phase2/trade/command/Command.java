package phase2.trade.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@CommandProperty(crudType = CRUDType.READ, isUndoable = true, persistent = true)
// please annotate your own in subclasses
public abstract class Command<T> implements PermissionBased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    boolean ifUndone = false;

    private Long timestamp;

    private Long undoTimestamp;

    @OneToOne
    protected User operator;

    // this one is to be persisted for effected entities and to be deserialized
    protected String effectedEntitiesToPersist;

    // this map is basically Class<?> -> A set of effected ids
    // the reason why this is not persisted in its native form is because doing so would potentially mess up the db structure
    // the String part has to be a class's name and the set has to contain all effected ids
    // maybe it would be possible to benefit from sql statements to figure out the overlapping records, but I don't know how to query all mapped columns of such a nested set inside a map
    protected transient Map<String, Set<String>> effectedEntities;

    protected transient GatewayBundle gatewayBundle;

    private boolean asynchronous;

    private transient CommandProperty commandPropertyAnnotation;

    public Command(GatewayBundle gatewayBundle, User operator) {
        this();
        this.gatewayBundle = gatewayBundle;
        this.effectedEntities = new HashMap<>();
        this.operator = operator;
    }

    public Command(GatewayBundle gatewayBundle) {
        this(gatewayBundle, new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).configureSystemUser());
    }

    public Command() {
        loadAnnotation();
    }

    public void loadAnnotation() {
        if (this.getClass().isAnnotationPresent(CommandProperty.class)) {
            commandPropertyAnnotation = this.getClass().getAnnotation(CommandProperty.class);
        }
    }

    public abstract void execute(StatusCallback<T> callback, String... args);

    public abstract void undo();

    public void redo() {
    } // It seems we don't need to implement redo. Also redo may mess up the uid

    // override this one if your command is not undoable, call FAILED
    public void isUndoable(StatusCallback<List<Command<?>>> callback) { // get all future commands that have an impact on the current one
        getEntityBundle().getCommandGateway().submitSession(() -> {
            List<Command<?>> futureCommands = getEntityBundle().getCommandGateway().getFutureCommands(timestamp);
            List<Command<?>> blockingCommands = new ArrayList<>();
            for (Command<?> command : futureCommands) {
                if (command.commandPropertyAnnotation.crudType().hasEffect && ifOverlaps(command.effectedEntitiesToPersist)) {
                    blockingCommands.add(command);
                }
            }
            blockingCommands.sort(new CommandComparator());
            if (blockingCommands.size() > 0) {
                callback.call(blockingCommands, ResultStatus.FAILED);
            } else {
                callback.call(blockingCommands, ResultStatus.SUCCEEDED);
            }
        });
    }

    protected void addEffectedEntity(Class<?> clazz, Long... ids) {
        for (Long id : ids) {
            putOrAdd(effectedEntities, clazz.getName(), id.toString());
        }
    }

    protected void save() {
        if (operator.getPermissionGroup().equals(PermissionGroup.SYSTEM))
            operator = null; // still the system user should be null
        timestamp = System.currentTimeMillis();
        effectedEntitiesToPersist = translateEffectedEntitiesToPersist(effectedEntities);
        getEntityBundle().getCommandGateway().submitTransaction(() -> getEntityBundle().getCommandGateway().add(getThis()));
    }

    protected void updateUndo() {
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        getEntityBundle().getCommandGateway().submitTransaction(() -> getEntityBundle().getCommandGateway().update(getThis()));
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, commandPropertyAnnotation.permissionSet()).checkPermission();
    }

    public boolean checkPermission(StatusCallback<?> statusCallback) {
        boolean result = checkPermission();
        if (!result) {
            statusCallback.call(null, ResultStatus.NO_PERMISSION);
        }
        return result;
    }

    private boolean ifOverlaps(String otherEffectedEntitiesToPersist) {
        Map<String, Set<String>> otherEffectedEntities = retrieveEffectedEntities(otherEffectedEntitiesToPersist);
        Map<String, Set<String>> effectedEntities = retrieveEffectedEntities(effectedEntitiesToPersist);
        for (Map.Entry<String, Set<String>> entry : otherEffectedEntities.entrySet()) {
            if (effectedEntities.containsKey(entry.getKey())) {
                if (!Collections.disjoint(effectedEntities.get(entry.getKey()), entry.getValue())) return true;
            }
        }
        return false;
    }

    private void putOrAdd(Map<String, Set<String>> map, String key, String value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            map.put(key, new HashSet<String>() {{
                add(value);
            }});
        }
    }

    private String translateEffectedEntitiesToPersist(Map<String, Set<String>> map) {
        StringBuilder temp = new StringBuilder();
        for (Map.Entry<String, Set<String>> entry : map.entrySet()) {
            temp.append(entry.getKey()).append("!").append(String.join(",", entry.getValue())).append(";");
        }
        return temp.toString();
    }

    private Map<String, Set<String>> retrieveEffectedEntities(String effected) {
        Map<String, Set<String>> temp = new HashMap<>();
        Pattern classNamePattern = Pattern.compile("(.*)!");
        Pattern idsPattern = Pattern.compile("(\\d+)([,]|$)");
        for (String record : effected.split(";")) {
            Matcher matcher = classNamePattern.matcher(record);
            String clazz = "undefined";
            Set<String> ids = new HashSet<>();
            if (matcher.find()) {
                clazz = matcher.group(1);
            }
            Matcher idsMatcher = idsPattern.matcher(record);
            while (idsMatcher.find()) {
                ids.add(idsMatcher.group(1));
            }
            temp.put(clazz, ids);
        }
        return temp;
    }

    protected EntityBundle getEntityBundle() {
        return gatewayBundle.getEntityBundle();
    }

    protected ConfigBundle getConfigBundle() {
        return gatewayBundle.getConfigBundle();
    }

    public Command<T> getThis() {
        return this;
    }

    public void setGatewayBundle(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }
}
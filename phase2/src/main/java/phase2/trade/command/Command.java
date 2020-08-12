package phase2.trade.command;

import phase2.trade.callback.*;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @param <T> The entity this command handles (supposed to return), this will be used to define the callback type
 * @author Dan Lyu
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@CommandProperty(crudType = CRUDType.READ, undoable = true, persistent = true)
// please annotate CommandProperty in subclasses, otherwise the one above will be used
public abstract class Command<T> implements PermissionBased {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    protected transient User operator;

    @OneToOne
    protected User userToPersist;

    private boolean ifUndone = false;

    private Long timestamp;

    private Long undoTimestamp;

    // this one is to be persisted for effected entities and to be deserialized
    protected String effectedEntitiesToPersist;

    // this map is basically Class<?> -> A set of effected ids
    // the reason why this is not persisted in its native form is because doing so would potentially mess up the db structure
    // the String part has to be a class's name and the set has to contain all effected ids
    // maybe it would be possible to benefit from sql statements to figure out the overlapping records, but I don't know how to query all mapped columns of such a nested set inside a map
    protected transient Map<String, Set<Long>> effectedEntities;

    protected transient GatewayBundle gatewayBundle;

    private transient boolean asynchronous = true;

    private transient CommandProperty commandPropertyAnnotation;

    void injectByFactory(GatewayBundle gatewayBundle, User operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
        persistUserIfNotSystem();
        System.out.println("Command <" + getClass().getSimpleName() + "> Created  |  Operator: " + operator.getUserName());
    }

    public Command() {
        loadAnnotation();
        effectedEntities = retrieveEffectedEntities(effectedEntitiesToPersist);
    }

    public void loadAnnotation() {
        if (this.getClass().isAnnotationPresent(CommandProperty.class)) {
            commandPropertyAnnotation = this.getClass().getAnnotation(CommandProperty.class);
        }
    }

    public abstract void execute(ResultStatusCallback<T> callback, String... args);

    public void undo() {
    }
    // do nothing here, isUndoable is supposed to be used by the outer world. So undo should no be directly called. Override this if undoable

    public void redo() {
    } // It seems we don't need to implement redo. Also redo may mess up the uid. Unless we store undo as new commands

    public void isUndoable(ResultStatusCallback<List<Command<?>>> callback) { // get all future commands that have an impact on the current one
        if (!commandPropertyAnnotation.undoable()) {
            callback.call(null, new StatusFailed());
            return;
        }
        getEntityBundle().getCommandGateway().submitSession((gateway) -> {
            List<Command<?>> futureCommands = gateway.getFutureCommands(timestamp);
            List<Command<?>> blockingCommands = new ArrayList<>();
            for (Command<?> command : futureCommands) {
                if (command.commandPropertyAnnotation.crudType().hasEffect && ifOverlaps(command.effectedEntitiesToPersist)) {
                    blockingCommands.add(command);
                }
            }
            blockingCommands.sort(new CommandComparator());
            if (blockingCommands.size() > 0) {
                callback.call(blockingCommands, new StatusFailed());
            } else {
                callback.call(blockingCommands, new StatusSucceeded());
            }
        });
    }

    protected void addEffectedEntity(Class<?> clazz, Long... ids) {
        for (Long id : ids) {
            putOrAdd(effectedEntities, clazz.getName(), id);
        }
    }

    protected Set<Long> getEffectedEntities(Class<?> clazz) {
        if (effectedEntities == null) retrieveEffectedEntities(effectedEntitiesToPersist);
        return effectedEntities.get(clazz.getName());
    }

    protected void save() {
        if (!commandPropertyAnnotation.persistent()) return;
        timestamp = System.currentTimeMillis();
        effectedEntitiesToPersist = translateEffectedEntitiesToPersist(effectedEntities);
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.add(getThis());
        });
    }

    // only used to avoid storing System as a user into database, this won't succeed also because System was not persistent as a User
    private void persistUserIfNotSystem(){
        if(operator.getPermissionGroup()!=PermissionGroup.SYSTEM){
            userToPersist = operator;
        }
    }

    protected void updateUndo() {
        if (!commandPropertyAnnotation.persistent()) return;
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.update(getThis());
        });
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, commandPropertyAnnotation.permissionSet(), commandPropertyAnnotation.permissionGroup()).checkPermission();
    }

    public boolean checkPermission(ResultStatusCallback<?> statusCallback) {
        boolean result = checkPermission();
        if (!result) {
            statusCallback.call(null, new StatusNoPermission(new PermissionSet(commandPropertyAnnotation.permissionSet())));
        }
        return result;
    }

    private boolean ifOverlaps(String otherEffectedEntitiesToPersist) {
        Map<String, Set<Long>> otherEffectedEntities = retrieveEffectedEntities(otherEffectedEntitiesToPersist);
        Map<String, Set<Long>> effectedEntities = retrieveEffectedEntities(effectedEntitiesToPersist);
        for (Map.Entry<String, Set<Long>> entry : otherEffectedEntities.entrySet()) {
            if (effectedEntities.containsKey(entry.getKey())) {
                if (!Collections.disjoint(effectedEntities.get(entry.getKey()), entry.getValue())) return true;
            }
        }
        return false;
    }

    private void putOrAdd(Map<String, Set<Long>> map, String key, Long value) {
        if (map.containsKey(key)) {
            map.get(key).add(value);
        } else {
            map.put(key, new HashSet<Long>() {{
                add(value);
            }});
        }
    }

    private String translateEffectedEntitiesToPersist(Map<String, Set<Long>> map) {
        StringBuilder temp = new StringBuilder();
        for (Map.Entry<String, Set<Long>> entry : map.entrySet()) {
            temp.append(entry.getKey()).append("!").append(String.join(",", String.valueOf(entry.getValue()))).append(";");
        }
        return temp.toString();
    }

    private Map<String, Set<Long>> retrieveEffectedEntities(String effected) {
        if (effected == null) return new HashMap<>();
        Map<String, Set<Long>> temp = new HashMap<>();
        Pattern classNamePattern = Pattern.compile("(.*)!");
        Pattern idsPattern = Pattern.compile("(\\d+)([,]|$)");
        for (String record : effected.split(";")) {
            Matcher matcher = classNamePattern.matcher(record);
            String clazz = "undefined";
            Set<Long> ids = new HashSet<>();
            if (matcher.find()) {
                clazz = matcher.group(1);
            }
            Matcher idsMatcher = idsPattern.matcher(record);
            while (idsMatcher.find()) {
                ids.add(Long.valueOf(idsMatcher.group(1)));
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
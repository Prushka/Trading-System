package phase2.trade.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusNoPermission;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.*;
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
public abstract class Command<T> implements PermissionBased, ArgsInvolved {

    private static final Logger logger = LogManager.getLogger(Command.class);

    private Long uid;

    protected User operator;

    private boolean undone = false;

    private Long timestamp;

    private Long undoTimestamp;

    private String dType;

    // this map is basically Class<?> -> A set of effected ids
    // the reason why this is not persisted in its native form is because doing so would potentially pollute the db structure
    // the String part has to be a class's name and the set has to contain all effected ids
    // maybe it would be possible to benefit from sql statements to figure out the overlapping records, but I don't know how to query all mapped columns of such a nested set inside a map
    protected transient Map<String, Set<Long>> effectedEntities = new HashMap<>();

    protected transient GatewayBundle gatewayBundle;

    private transient boolean asynchronous = true;

    private transient CommandProperty commandPropertyAnnotation;

    void injectByFactory(GatewayBundle gatewayBundle, User operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
        logger.debug("Command <" + getClass().getSimpleName() + "> Created  |  Operator: " + operator.getName() + "  |  " + operator.getPermissionGroup() + "  |  " + operator.getPermissionSet().getPerm().toString());
    }

    public Command() {
        loadAnnotation();
    }

    public void loadAnnotation() {
        if (this.getClass().isAnnotationPresent(CommandProperty.class)) {
            commandPropertyAnnotation = this.getClass().getAnnotation(CommandProperty.class);
        }
    }

    public abstract void execute(ResultStatusCallback<T> callback, String... args);

    protected void undoUnchecked() {
    }
    // do nothing here, undoIfUndoable is supposed to be used by the outer world. So undoUnchecked should no be directly called. Override this if undoable

    public void redo() {
    }
    // It seems we don't need to implement redo. Also redo may mess up the uid. Unless we store undo as new commands

    public void undoIfUndoable(ResultStatusCallback<List<Command>> callback, GatewayBundle gatewayBundle) { // get all future commands that have an impact on the current one
        if (!commandPropertyAnnotation.undoable()) {
            callback.call(null, new StatusFailed());
            return;
        }
        this.gatewayBundle = gatewayBundle;
        getEntityBundle().getCommandGateway().submitSession((gateway) -> {
            List<Command> futureCommands = gateway.getFutureCommands(timestamp);
            List<Command> blockingCommands = new ArrayList<>();
            for (Command command : futureCommands) {
                if (command.commandPropertyAnnotation.crudType().hasEffect && ifOverlaps(command.effectedEntities)) {
                    blockingCommands.add(command);
                }
            }
            blockingCommands.sort(new CommandComparator());
            if (blockingCommands.size() > 0) {
                callback.call(blockingCommands, new StatusFailed());
            } else {
                undoUnchecked();
                callback.call(blockingCommands, new StatusSucceeded());
            }
        });
    }

    protected void save() {
        if (!commandPropertyAnnotation.persistent()) return;
        timestamp = System.currentTimeMillis();
        // effectedEntitiesToPersist = translateEffectedEntitiesToPersist(effectedEntities);
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.add(getThis());
        });
    }

    protected void updateUndo() {
        if (!commandPropertyAnnotation.persistent()) return;
        setUndoTimestamp(System.currentTimeMillis());
        setUndone(true);
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.update(getThis());
        });
    }

    public boolean checkPermission(Permission... permissions) {
        return new UserPermissionChecker(operator, permissions, commandPropertyAnnotation.permissionGroup()).checkPermission();
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, commandPropertyAnnotation.permissionSet(), commandPropertyAnnotation.permissionGroup()).checkPermission();
    }

    public boolean checkPermission(ResultStatusCallback<?> statusCallback, Permission... permissions) {
        boolean result = checkPermission(permissions);
        if (!result) {
            logger.warn("[Permission Denied] User: " + operator + " | " + operator.getPermissionGroup() + " | " + Arrays.toString(permissions) + " -> " + operator.getPermissionSet().getPerm().toString());
            statusCallback.call(null, new StatusNoPermission(new PermissionSet(permissions)));
        }
        return result;
    }

    public boolean checkPermission(ResultStatusCallback<?> statusCallback) {
        return checkPermission(statusCallback, commandPropertyAnnotation.permissionSet());
    }

    private boolean ifOverlaps(Map<String, Set<Long>> otherEffectedEntities) {
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
        Pattern idsPattern = Pattern.compile("(\\d+)([,]|[]]|$)");
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

    protected void addEffectedEntity(Class<?> clazz, Long... ids) {
        for (Long id : ids) {
            putOrAdd(effectedEntities, clazz.getName(), id);
        }
    }

    public Set<Long> getEffectedEntities(Class<?> clazz) {
        return effectedEntities.get(clazz.getName());
    }

    protected Long getOneEntity(Class<?> clazz) {
        return getEffectedEntities(clazz).iterator().next();
    }

    @Transient
    protected EntityBundle getEntityBundle() {
        return gatewayBundle.getEntityBundle();
    }

    @Transient
    protected ConfigBundle getConfigBundle() {
        return gatewayBundle.getConfigBundle();
    }

    @Transient
    public Command<T> getThis() {
        return this;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Transient
    public boolean isAsynchronous() {
        return asynchronous;
    }

    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    @OneToOne
    public User getOperator() {
        if (operator == null || operator.getPermissionGroup() == PermissionGroup.SYSTEM) return null;
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    @Column(insertable = false, updatable = false)
    public String getDType() {
        return dType;
    }

    public void setDType(String dType) {
        this.dType = dType;
    }

    public void setEffectedEntitiesToPersist(String toPersist) {
        this.effectedEntities = retrieveEffectedEntities(toPersist);
    }

    public String getEffectedEntitiesToPersist() {
        return translateEffectedEntitiesToPersist(effectedEntities);
    }

    public boolean isUndone() {
        return undone;
    }

    public void setUndone(boolean ifUndone) {
        this.undone = ifUndone;
    }

    public Long getUndoTimestamp() {
        return undoTimestamp;
    }

    public void setUndoTimestamp(Long undoTimestamp) {
        this.undoTimestamp = undoTimestamp;
    }

    // https://stackoverflow.com/questions/13874528/what-is-the-purpose-of-accesstype-field-accesstype-property-and-access/13874900#13874900
    // The default is not FIELD. The access type is FIELD if you place mapping annotations on fields, and it's PROPERTY if you place mapping annotations on getters. And all the entity hierarchy must be coherent in the mapping annotation placement: always on fields, or always on getters, but not mixed.
}
package phase2.trade.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusNoPermission;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.EntityGatewayBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.permission.*;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * The Command base class that implements the Command design pattern.<p>
 * Ideally every use case method should extend Command,
 * tell this class the entities its execution has impact on and implement its own {@link Command#execute} and {@link Command#undoUnchecked} methods.<p>
 * This class will use the subclass's annotation {@link CommandProperty}, if the annotation is not annotated, the annotation of this class will be used instead.<p>
 * The annotation defines the required permissions to execute, is undoable, persistent and {@link CRUDType}).<p>
 * This class handles the undo overlap automatically as long as the affected entity types and ids are set in the subclasses.<p>
 * It also checks the permission of the operator. However the execute method in subclasses are required to call {@link Command#checkPermission()} manually.
 *
 * @param <T> the entity type this Command handles. It defines a toUpdate fields and the callback type
 * @author Dan Lyu
 * @see CommandProperty
 * @see UpdateCommand
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@CommandProperty(crudType = CRUDType.READ, undoable = true, persistent = true)
// please annotate CommandProperty in subclasses, otherwise the one above will be used
public abstract class Command<T> implements PermissionBased, ArgsInvolved {

    private static final Logger logger = LogManager.getLogger(Command.class);

    private Long uid;

    /**
     * The Operator.
     */
    protected User operator;

    private boolean undone = false;

    private Long timestamp;

    private Long undoTimestamp;

    private String dType;

    /**
     * The Affected entities.
     */
// this map is basically Class<?> -> A set of effected ids
    // the reason why this is not persisted in its native form is because doing so would potentially pollute the db structure
    // the String part has to be a class's name and the set has to contain all effected ids
    // maybe it would be possible to benefit from sql statements to figure out the overlapping records, but I don't know how to query all mapped columns of such a nested set inside a map
    protected transient Map<String, Set<Long>> affectedEntities = new HashMap<>();

    /**
     * The Gateway bundle.
     */
    protected transient GatewayBundle gatewayBundle;

    private transient boolean asynchronous = true;

    private transient CommandProperty commandPropertyAnnotation;

    /**
     * Inject by factory.
     *
     * @param gatewayBundle the gateway bundle
     * @param operator      the operator
     */
    void injectByFactory(GatewayBundle gatewayBundle, User operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
        logger.debug("Command <" + getClass().getSimpleName() + "> Created  |  Operator: " + operator.getName() + "  |  " + operator.getPermissionGroup() + "  |  " + operator.getPermissionSet().getPerm().toString());
    }

    /**
     * Constructs a new Command.
     */
    public Command() {
        loadAnnotation();
    }

    /**
     * Load annotation.
     */
    public void loadAnnotation() {
        if (this.getClass().isAnnotationPresent(CommandProperty.class)) {
            commandPropertyAnnotation = this.getClass().getAnnotation(CommandProperty.class);
        }
    }

    /**
     * Execute.
     *
     * @param callback the callback
     * @param args     the args
     */
    public abstract void execute(ResultStatusCallback<T> callback, String... args);

    /**
     * Override this method in subclasses if your class is undoable.
     */
    protected void undoUnchecked() {
    }
    // do nothing here, undoIfUndoable is supposed to be used by the outer world. So undoUnchecked should no be directly called. Override this if undoable

    /**
     * Undo if undoable. A list of future Commands that block the undo execution of the current Command will be returned. If no such Command exists, a list with size 0 will be returned.
     *
     * @param callback      the callback
     * @param gatewayBundle the gateway bundle
     */
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
                if (command.commandPropertyAnnotation.crudType().willAffect && ifOverlaps(command.affectedEntities)) {
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

    /**
     * Save.
     */
    protected void save() {
        if (!commandPropertyAnnotation.persistent()) return;
        timestamp = System.currentTimeMillis();
        // effectedEntitiesToPersist = translateAffectedEntitiesToPersist(effectedEntities);
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.add(getThis());
        });
    }

    /**
     * Update undo.
     */
    protected void updateUndo() {
        if (!commandPropertyAnnotation.persistent()) return;
        setUndoTimestamp(System.currentTimeMillis());
        setUndone(true);
        getEntityBundle().getCommandGateway().submitTransaction((gateway) -> {
            gateway.update(getThis());
        });
    }

    /**
     * Check permission boolean.
     *
     * @param permissions the permissions
     * @return the boolean
     */
    public boolean checkPermission(Permission... permissions) {
        return new UserPermissionChecker(operator, permissions, commandPropertyAnnotation.permissionGroup()).checkPermission();
    }

    @Override
    public boolean checkPermission() {
        return new UserPermissionChecker(operator, commandPropertyAnnotation.permissionSet(), commandPropertyAnnotation.permissionGroup()).checkPermission();
    }

    /**
     * Check permission boolean.
     *
     * @param statusCallback the status callback
     * @param permissions    the permissions
     * @return the boolean
     */
    public boolean checkPermission(ResultStatusCallback<?> statusCallback, Permission... permissions) {
        boolean result = checkPermission(permissions);
        if (!result) {
            statusCallback.call(null, new StatusNoPermission(new PermissionSet(permissions)));
        }
        return result;
    }

    /**
     * Check permission boolean.
     *
     * @param statusCallback the status callback
     * @return the boolean
     */
    public boolean checkPermission(ResultStatusCallback<?> statusCallback) {
        return checkPermission(statusCallback, commandPropertyAnnotation.permissionSet());
    }

    private boolean ifOverlaps(Map<String, Set<Long>> otherEffectedEntities) {
        for (Map.Entry<String, Set<Long>> entry : otherEffectedEntities.entrySet()) {
            if (affectedEntities.containsKey(entry.getKey())) {
                if (!Collections.disjoint(affectedEntities.get(entry.getKey()), entry.getValue())) return true;
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

    private String translateAffectedEntitiesToPersist(Map<String, Set<Long>> map) {
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

    /**
     * Add effected entity.
     *
     * @param clazz the clazz
     * @param ids   the ids
     */
    protected void addEffectedEntity(Class<?> clazz, Long... ids) {
        for (Long id : ids) {
            putOrAdd(affectedEntities, clazz.getName(), id);
        }
    }

    /**
     * Gets effected entities.
     *
     * @param clazz the clazz
     * @return the effected entities
     */
    public Set<Long> getEffectedEntities(Class<?> clazz) {
        return affectedEntities.get(clazz.getName());
    }

    /**
     * Gets one entity.
     *
     * @param clazz the clazz
     * @return the one entity
     */
    protected Long getOneEntity(Class<?> clazz) {
        return getEffectedEntities(clazz).iterator().next();
    }

    /**
     * Gets entity bundle.
     *
     * @return the entity bundle
     */
    @Transient
    protected EntityGatewayBundle getEntityBundle() {
        return gatewayBundle.getEntityBundle();
    }

    /**
     * Gets config bundle.
     *
     * @return the config bundle
     */
    @Transient
    protected ConfigBundle getConfigBundle() {
        return gatewayBundle.getConfigBundle();
    }

    /**
     * Gets this.
     *
     * @return the this
     */
    @Transient
    public Command<T> getThis() {
        return this;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public Long getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Is asynchronous boolean.
     *
     * @return the boolean
     */
    @Transient
    public boolean isAsynchronous() {
        return asynchronous;
    }

    /**
     * Sets asynchronous.
     *
     * @param asynchronous the asynchronous
     */
    public void setAsynchronous(boolean asynchronous) {
        this.asynchronous = asynchronous;
    }

    /**
     * Gets operator.
     *
     * @return the operator
     */
    @OneToOne
    public User getOperator() {
        if (operator == null || operator.getPermissionGroup() == PermissionGroup.SYSTEM) return null;
        return operator;
    }

    /**
     * Sets operator.
     *
     * @param operator the operator
     */
    public void setOperator(User operator) {
        this.operator = operator;
    }

    /**
     * Gets d type.
     *
     * @return the d type
     */
    @Column(insertable = false, updatable = false)
    public String getDType() {
        return dType;
    }

    /**
     * Sets d type.
     *
     * @param dType the d type
     */
    public void setDType(String dType) {
        this.dType = dType;
    }

    /**
     * Sets affected entities to persist.
     *
     * @param toPersist the to persist
     */
    public void setAffectedEntitiesToPersist(String toPersist) {
        this.affectedEntities = retrieveEffectedEntities(toPersist);
    }

    /**
     * Gets affected entities to persist.
     *
     * @return the affected entities to persist
     */
    public String getAffectedEntitiesToPersist() {
        return translateAffectedEntitiesToPersist(affectedEntities);
    }

    /**
     * Is undone boolean.
     *
     * @return the boolean
     */
    public boolean isUndone() {
        return undone;
    }

    /**
     * Sets undone.
     *
     * @param ifUndone the if undone
     */
    public void setUndone(boolean ifUndone) {
        this.undone = ifUndone;
    }

    /**
     * Gets undo timestamp.
     *
     * @return the undo timestamp
     */
    public Long getUndoTimestamp() {
        return undoTimestamp;
    }

    /**
     * Sets undo timestamp.
     *
     * @param undoTimestamp the undo timestamp
     */
    public void setUndoTimestamp(Long undoTimestamp) {
        this.undoTimestamp = undoTimestamp;
    }
}
package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Command<T> {

    public enum Type {
        CREATE(true), READ(false), UPDATE(true), DELETE(true);

        public boolean hasEffect;

        Type(boolean hasEffect) {
            this.hasEffect = hasEffect;
        }
    }

    boolean ifUndone = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private Long timestamp;

    private Long undoTimestamp;

    @OneToOne
    User operator;

    @ElementCollection
    Set<Long> effectedIds = new HashSet<>();

    transient GatewayBundle gatewayBundle;

    public Command(GatewayBundle gatewayBundle, User operator) {
        this.operator = operator;
        this.gatewayBundle = gatewayBundle;
    }

    public Command() {

    }

    public boolean checkPermission() {
        boolean all = true;
        for (Permission permissionRequired : getPermissionRequired().getPerm()) {
            all = all && operator.hasPermission(permissionRequired);
        }
        return all;
    }

    public abstract PermissionSet getPermissionRequired();

    public abstract void execute(Callback<T> callback, String... args);

    public abstract void undo();

    public abstract void redo();

    void save() {
        timestamp = System.currentTimeMillis();
        gatewayBundle.getCommandGateway().submitTransaction(() -> gatewayBundle.getCommandGateway().add(getThis()));
    }

    void updateUndo() {
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        gatewayBundle.getCommandGateway().submitTransaction(() -> gatewayBundle.getCommandGateway().update(getThis()));
    }

    public Command<T> getThis() {
        return this;
    }

    public GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }

    public void setGatewayBundle(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public void addEffectedId(Long id) {
        effectedIds.add(id);
    }

    public abstract Class<?> getClassToOperateOn();

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public User getOperator() {
        return operator;
    }

    public void setOperator(User operator) {
        this.operator = operator;
    }

    public Set<Long> getEffectedIds() {
        return effectedIds;
    }

    public void setEffectedIds(Set<Long> effectedIds) {
        this.effectedIds = effectedIds;
    }

    public abstract Type getCommandType();

    public Long getTimestamp() {
        return timestamp;
    }

    public void isUndoable(Callback<List<Command<?>>> callback) {
        gatewayBundle.getCommandGateway().submitSession(() -> callback.call(gatewayBundle.getCommandGateway().isUndoable(effectedIds, timestamp)));
    }

    public boolean isIfUndone() {
        return ifUndone;
    }

    public void setIfUndone(boolean ifUndone) {
        this.ifUndone = ifUndone;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUndoTimestamp() {
        return undoTimestamp;
    }

    public void setUndoTimestamp(Long undoTimestamp) {
        this.undoTimestamp = undoTimestamp;
    }
}
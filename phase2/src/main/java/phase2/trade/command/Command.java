package phase2.trade.command;

import phase2.trade.callback.Callback;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Command<T> {

    @Entity
    protected static class CommandData<Q> {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long uid;

        Class<Q> clazz;

        Long id;

        public CommandData() {

        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    boolean ifUndone = false;

    private Long timestamp;

    private Long undoTimestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    protected Collection<Long> effectedIds;

    protected transient GatewayBundle gatewayBundle;

    public Command(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
        this.effectedIds = new HashSet<>();
    }

    public Command() {

    }


    public abstract void execute(StatusCallback<T> callback, String... args);

    public abstract void undo();

    public abstract void redo(); // It seems we don't need to implement redo. Also redo may mess up the uid

    public abstract Class<?> getClassToOperateOn();

    public abstract CRUDType getCRUDType();

    protected void save() {
        timestamp = System.currentTimeMillis();
        getEntityBundle().getCommandGateway().submitTransaction(() -> getEntityBundle().getCommandGateway().add(getThis()));
    }

    protected void updateUndo() {
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        getEntityBundle().getCommandGateway().submitTransaction(() -> getEntityBundle().getCommandGateway().update(getThis()));
    }

    public Command<T> getThis() {
        return this;
    }

    public void setGatewayBundle(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public void addEffectedId(Long... ids) {
        effectedIds.addAll(Arrays.asList(ids));
    }

    public Collection<Long> getEffectedIds() {
        return effectedIds;
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

    public void isUndoable(Callback<List<Command<?>>> callback) {
        getEntityBundle().getCommandGateway().submitSession(() -> callback.call(getEntityBundle().getCommandGateway().isUndoable(effectedIds, timestamp)));
    }

    protected EntityBundle getEntityBundle() {
        return gatewayBundle.getEntityBundle();
    }

    protected ConfigBundle getConfigBundle() {
        return gatewayBundle.getConfigBundle();
    }
}
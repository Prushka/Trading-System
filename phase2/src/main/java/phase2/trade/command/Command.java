package phase2.trade.command;

import phase2.trade.callback.Callback;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;

import javax.persistence.*;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Command<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    boolean ifUndone = false;

    private Long timestamp;

    private Long undoTimestamp;

    @ElementCollection(fetch = FetchType.EAGER)
    Collection<Long> effectedIds;

    protected transient EntityBundle entityBundle;

    public Command(EntityBundle entityBundle) {
        this.entityBundle = entityBundle;
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
        entityBundle.getCommandGateway().submitTransaction(() -> entityBundle.getCommandGateway().add(getThis()));
    }

    protected void updateUndo() {
        undoTimestamp = System.currentTimeMillis();
        ifUndone = true;
        entityBundle.getCommandGateway().submitTransaction(() -> entityBundle.getCommandGateway().update(getThis()));
    }

    public Command<T> getThis() {
        return this;
    }

    public void setEntityBundle(EntityBundle entityBundle) {
        this.entityBundle = entityBundle;
    }

    public void addEffectedId(Long id) {
        effectedIds.add(id);
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
        entityBundle.getCommandGateway().submitSession(() -> callback.call(entityBundle.getCommandGateway().isUndoable(effectedIds, timestamp)));
    }
}
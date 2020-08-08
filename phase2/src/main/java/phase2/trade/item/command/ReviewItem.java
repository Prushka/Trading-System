package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public class ReviewItem extends ItemCommand {

    private Long itemId;

    private transient User operator;

    private Ownership oldOwnership;

    public ReviewItem(EntityBundle entityBundle, RegularUser operator, Long itemId) {
        super(entityBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
    }

    public ReviewItem() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { //
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        entityBundle.getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);

            oldOwnership = item.getOwnership();
            item.setOwnership(Ownership.OWNER);
            entityBundle.getItemGateway().update(item);
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        entityBundle.getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);
            item.setOwnership(oldOwnership);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}

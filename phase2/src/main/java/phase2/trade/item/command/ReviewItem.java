package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.REVIEW_ITEM})
public class ReviewItem extends ItemCommand<Item> {

    private Long itemId;

    private Ownership oldOwnership;

    public ReviewItem(GatewayBundle gatewayBundle, User operator, Long itemId) {
        super(gatewayBundle, operator);
        this.itemId = itemId;
    }

    public ReviewItem() {}

    @Override
    public void execute(StatusCallback<Item> callback, String... args) { //
        if (!checkPermission()) {
            callback.call(null, ResultStatus.NO_PERMISSION);
            return;
        }
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);

            oldOwnership = item.getOwnership();
            item.setOwnership(Ownership.OWNER);
            getEntityBundle().getItemGateway().update(item);
            addEffectedEntity(Item.class, itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);
            item.setOwnership(oldOwnership);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }
}

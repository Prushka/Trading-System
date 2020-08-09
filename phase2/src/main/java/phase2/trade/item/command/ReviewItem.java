package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public class ReviewItem extends ItemCommand<Item> {

    private Long itemId;

    private transient User operator;

    private Ownership oldOwnership;

    public ReviewItem(GatewayBundle gatewayBundle, RegularUser operator, Long itemId) {
        super(gatewayBundle, operator);
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
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);

            oldOwnership = item.getOwnership();
            item.setOwnership(Ownership.OWNER);
            getEntityBundle().getItemGateway().update(item);
            addEffectedId(itemId);
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


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}
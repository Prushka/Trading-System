package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.callback.StatusCallback;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.*;

@Entity
public class RemoveItem extends ItemCommand<Long[]> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private transient RegularUser operator;

    private Ownership oldOwnership;

    private ItemListType itemListType;

    public RemoveItem(GatewayBundle gatewayBundle, RegularUser operator, ItemListType itemListType, Set<Long> itemIds) {
        super(gatewayBundle, operator);
        this.itemIds = itemIds;
        this.operator = operator;
        this.itemListType = itemListType;
    }

    public RemoveItem() {
        super();
    }

    @Override
    public void execute(StatusCallback<Long[]> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Long[] ids = itemIds.toArray(new Long[0]);
            operator.getItemList(itemListType).removeItemByUid(ids);
            getEntityBundle().getUserGateway().update(operator);
            addEffectedEntity(Item.class, ids);
            save();
            if (callback != null)
                callback.call(ids, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
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

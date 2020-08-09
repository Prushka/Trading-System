package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import java.util.Set;

@Entity
public class AlterWillingness extends ItemCommand<Item> {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Long> itemIds;

    private Willingness newWillingness;

    // private Willingness oldWillingness;

    private transient RegularUser operator;

    public AlterWillingness(GatewayBundle gatewayBundle, RegularUser operator, Willingness newWillingness, Set<Long> itemIds) {
        super(gatewayBundle, operator);
        this.itemIds = itemIds;
        this.operator = operator;
        this.newWillingness = newWillingness;

    }

    public AlterWillingness() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Long[] ids = itemIds.toArray(new Long[0]);
            for (Item item : operator.getItemList(ItemListType.INVENTORY).getListOfItems()) {
                if (itemIds.contains(item.getUid())) {
                    item.setWillingness(newWillingness);
                }
            }
            getEntityBundle().getUserGateway().update(operator);
            addEffectedEntity(Item.class, ids);
            save();
            if (callback != null)
                callback.call(null, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }


    @Override
    public void undo() {
    }

    @Override
    public void redo() {

    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}

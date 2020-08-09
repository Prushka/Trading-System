package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AddItemToItemList extends ItemCommand<Item> {

    private ItemListType itemListType;

    private Long itemId;

    private transient RegularUser operator;

    public AddItemToItemList(GatewayBundle gatewayBundle, RegularUser operator,
                             ItemListType itemListType) {
        super(gatewayBundle, operator);
        this.itemListType = itemListType;
        this.operator = operator;
    }

    public AddItemToItemList() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        getEntityBundle().getUserGateway().submitTransaction(() -> {
            Item item = new Item();
            item.setName(args[0]);
            item.setDescription(args[1]);
            item.setCategory(Category.valueOf(args[2]));
            item.setItemList(operator.getItemList(itemListType));
            item.setOwner(operator);
            operator.getItemList(itemListType).addItem(item);
            getEntityBundle().getUserGateway().update(operator);
            this.itemId = item.getUid();
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public void undo() {
        getEntityBundle().getItemGateway().submitTransaction(() -> {
            getEntityBundle().getItemGateway().delete(itemId);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public CRUDType getCRUDType() {
        return CRUDType.CREATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}

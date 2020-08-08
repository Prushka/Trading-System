package phase2.trade.item.command;

import phase2.trade.command.CRUDType;
import phase2.trade.gateway.Callback;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AlterItem extends ItemCommand {


    private Long itemId;

    private transient RegularUser operator;

    public AlterItem(EntityBundle entityBundle, RegularUser operator, Long itemId) {
        super(entityBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
    }

    public AlterItem() {
        super();
    }

    @Override
    public void execute(Callback<Item> callback, String... args) { //
        entityBundle.getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);
            item.setName(args[0]);
            item.setDescription(args[1]);
            entityBundle.getItemGateway().update(item);
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item);
        });
    }

    @Override
    public void undo() {
        entityBundle.getItemGateway().submitTransaction(() -> {
            entityBundle.getItemGateway().delete(itemId);
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

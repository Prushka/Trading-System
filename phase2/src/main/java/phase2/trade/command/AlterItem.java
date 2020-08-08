package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.PersonalUser;

import javax.persistence.Entity;

@Entity
public class AlterItem extends ItemCommand {


    private Long itemId;

    private transient PersonalUser operator;

    public AlterItem(GatewayBundle gatewayBundle, PersonalUser operator, Long itemId) {
        super(gatewayBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
    }

    public AlterItem() {
        super();
    }

    @Override
    public void execute(Callback<Item> callback, String... args) { //
        gatewayBundle.getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInItemGateway(itemId);
            item.setName(args[0]);
            item.setDescription(args[1]);
            gatewayBundle.getItemGateway().update(item);
            addEffectedId(itemId);
            save();
            if (callback != null)
                callback.call(item);
        });
    }

    @Override
    public void undo() {
        gatewayBundle.getItemGateway().submitTransaction(() -> {
            gatewayBundle.getItemGateway().delete(itemId);
            updateUndo();
        });
    }

    @Override
    public void redo() {

    }


    @Override
    public Type getCommandType() {
        return Type.UPDATE;
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }
}

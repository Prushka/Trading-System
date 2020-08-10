package phase2.trade.item.command;

import phase2.trade.command.Command;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.Entity;

@Entity
public abstract class ItemCommand<T> extends Command<T>  {

    public ItemCommand(GatewayBundle gatewayBundle, User operator) {
        super(gatewayBundle, operator);
    }

    public ItemCommand() {

    }

    Item findItemByIdSyncInsideItemGateway(Long itemId) {
        return getEntityBundle().getItemGateway().findById(itemId);
    }

    Item findItemByIdSyncOutsideItemGateway(Long itemId) {
        getEntityBundle().getItemGateway().openCurrentSession();
        Item item = getEntityBundle().getItemGateway().findById(itemId);
        getEntityBundle().getItemGateway().closeCurrentSession();
        return item;
    }

}

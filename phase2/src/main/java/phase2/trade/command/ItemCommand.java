package phase2.trade.command;

import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.User;

import javax.persistence.Entity;

public abstract class ItemCommand extends Command<Item> {

    @Entity
    static class ItemCommandData extends CommandData<Item> {
        InventoryType inventoryType;

        Long itemId;
    }

    public ItemCommand(GatewayBundle gatewayBundle, Class<Item> clazz, User operator) {
        super(gatewayBundle, clazz, operator);
    }

    Item findItemByIdSync(Long itemId) {
        gatewayBundle.getItemGateway().openCurrentSessionWithTransaction();
        Item item = gatewayBundle.getItemGateway().findById(itemId);
        gatewayBundle.getItemGateway().closeCurrentSessionWithTransaction();
        return item;
    }

}

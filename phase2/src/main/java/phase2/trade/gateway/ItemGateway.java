package phase2.trade.gateway;

import phase2.trade.item.Item;

import java.util.List;

public interface ItemGateway extends EntityGateway<Item, ItemGateway> {
    List<Item> findByName(String itemName);

    List<Item> findMarketItems();
}

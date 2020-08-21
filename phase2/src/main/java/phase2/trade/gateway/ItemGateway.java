package phase2.trade.gateway;

import phase2.trade.item.Item;

import java.util.List;

/**
 * The {@link Item} gateway interface.
 *
 * @author Dan Lyu
 * @see Item
 */
public interface ItemGateway extends EntityGateway<Item, ItemGateway> {

    /**
     * Find items that can be displayed in the market.
     *
     * @return the list
     */
    List<Item> findMarketItems();
}

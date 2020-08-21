package phase2.trade.item;

import phase2.trade.itemlist.ItemListType;
import phase2.trade.user.User;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The Item filter.
 *
 * @author Dan Lyu
 */
public class ItemFilter {

    private final User user;

    /**
     * Constructs a new Item filter.
     *
     * @param user the user
     */
    public ItemFilter(User user) {
        this.user = user;
    }

    /**
     * Gets tradable items.
     *
     * @return the tradable items
     */
    public Collection<Item> getTradableItems() {
        Collection<Item> items = user.getItemList(ItemListType.INVENTORY).getSetOfItems().stream().
                filter(p ->
                        p.getOwnership() == Ownership.OWNER
                ).collect(Collectors.toList());
        return items;
    }
}

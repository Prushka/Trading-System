package phase2.trade.item;

import phase2.trade.inventory.ItemListType;
import phase2.trade.user.User;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFilter {

    private final User user;

    public ItemFilter(User user) {
        this.user = user;
    }

    public Collection<Item> getTradableItems() {
        Collection<Item> items = user.getItemList(ItemListType.INVENTORY).getSetOfItems().stream().
                filter(p ->
                        p.getOwnership() == Ownership.TO_BE_REVIEWED
                ).collect(Collectors.toList());
        return items;
    }
}

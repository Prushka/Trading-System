package phase2.trade.itemlist;

import phase2.trade.item.CartItemWrapper;
import phase2.trade.item.Item;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The Cart.
 *
 * @author Dan Lyu
 */
@Entity

public class Cart extends ItemList {

    /**
     * The Set of items.
     */
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    protected Set<CartItemWrapper> setOfItems = new HashSet<>();

    @Override
    public Set<Item> getSetOfItems() {
        return setOfItems.stream().map(CartItemWrapper::getItem).collect(Collectors.toSet());
    }

    @Override
    public void setSetOfItems(Set<Item> items) {
        items.forEach(item -> setOfItems.add(new CartItemWrapper(item, item.getQuantity())));
    }

    /**
     * Find quantity int.
     *
     * @param item the item
     * @return the int
     */
    public int findQuantity(Item item) {

        Set<CartItemWrapper> toFind = setOfItems.stream().filter(cartItemWrapper -> cartItemWrapper.getItem().getUid().equals(item.getUid())).collect(Collectors.toSet());
        if (toFind.size() > 0)
            return toFind.iterator().next().getQuantity();
        else {
            return item.getQuantity();
        }
    }

    /**
     * Add item with quantity.
     *
     * @param item     the item
     * @param quantity the quantity
     */
    public void addItemWithQuantity(Item item, int quantity) {
        setOfItems.add(new CartItemWrapper(item, quantity));
    }


    public void removeItemByUid(Collection<Long> ids) {
        Set<CartItemWrapper> toRemove = new HashSet<>();
        for (Long uid : ids) {
            for (CartItemWrapper cartItemWrapper : setOfItems) {
                if (cartItemWrapper.getUid().equals(uid)) {
                    toRemove.add(cartItemWrapper);
                }
            }
        }
        setOfItems.removeAll(toRemove);
    }
}

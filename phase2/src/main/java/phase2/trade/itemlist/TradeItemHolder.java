package phase2.trade.itemlist;

import phase2.trade.item.Item;
import phase2.trade.item.Willingness;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

// This is held by the Item owner in a TradeOrder's UserOrderBundle
@Entity
public class TradeItemHolder extends ItemList {

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    protected Set<Item> setOfItems = new HashSet<>();

    @Override
    public Set<Item> getSetOfItems() {
        return setOfItems;
    }

    @Override
    public void setSetOfItems(Set<Item> items) {
        this.setOfItems = items;
    }

    public int getLendCount() {
        return Math.toIntExact(setOfItems.stream().filter(item -> item.getWillingness().equals(Willingness.LEND)).count());
    }

    public int getSellCount() {
        return Math.toIntExact(setOfItems.stream().filter(item -> item.getWillingness().equals(Willingness.SELL)).count());
    }
}

package phase2.trade.itemlist;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User owner;

    public abstract Set<Item> getSetOfItems();

    public abstract void setSetOfItems(Set<Item> items);

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addItem(Item... items) {
        getSetOfItems().addAll(Arrays.asList(items));
    }

    public void addItem(Collection<Item> items) {
        this.getSetOfItems().addAll(items);
    }

    public Long getId() {
        return id;
    }

    public int size() {
        return getSetOfItems().size();
    }

    public void removeItem(Item... items) {
        for (Item item : items) {
            this.getSetOfItems().remove(item);
        }
    }

    public void removeItemByUid(Collection<Long> ids) {
        for (Long uid : ids) {
            Item item = findByUid(uid);
            if (item != null) removeItem(item);
        }
    }

    public void removeItemByUid(Long... ids) {
        removeItemByUid(Arrays.asList(ids));
    }

    public Item findByUid(Long id) {
        for (Item item : getSetOfItems()) {
            if (item.getUid().equals(id)) return item;
        }
        return null;
    }

    public Collection<Long> getItemsAsIds() {
        Collection<Long> itemIds = new HashSet<>();
        getSetOfItems().forEach(item -> {
            itemIds.add(item.getUid());
        });
        return itemIds;
    }

    public boolean containsUid(Long id) {
        return findByUid(id) != null;
    }
}

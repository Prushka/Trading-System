package phase2.trade.itemlist;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The Item list.
 *
 * @author Dan Lyu
 */
@MappedSuperclass
public abstract class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private User owner;

    /**
     * Gets set of items.
     *
     * @return the set of items
     */
    public abstract Set<Item> getSetOfItems();

    /**
     * Sets set of items.
     *
     * @param items the items
     */
    public abstract void setSetOfItems(Set<Item> items);

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Add item.
     *
     * @param items the items
     */
    public void addItem(Item... items) {
        getSetOfItems().addAll(Arrays.asList(items));
    }

    /**
     * Add item.
     *
     * @param items the items
     */
    public void addItem(Collection<Item> items) {
        this.getSetOfItems().addAll(items);
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Size int.
     *
     * @return the int
     */
    public int size() {
        return getSetOfItems().size();
    }

    /**
     * Remove item.
     *
     * @param items the items
     */
    public void removeItem(Item... items) {
        for (Item item : items) {
            this.getSetOfItems().remove(item);
        }
    }

    /**
     * Remove item by uid.
     *
     * @param ids the ids
     */
    public void removeItemByUid(Collection<Long> ids) {
        for (Long uid : ids) {
            Item item = findByUid(uid);
            if (item != null) removeItem(item);
        }
    }

    /**
     * Remove item by uid.
     *
     * @param ids the ids
     */
    public void removeItemByUid(Long... ids) {
        removeItemByUid(Arrays.asList(ids));
    }

    /**
     * Find by uid item.
     *
     * @param id the id
     * @return the item
     */
    public Item findByUid(Long id) {
        for (Item item : getSetOfItems()) {
            if (item.getUid().equals(id)) return item;
        }
        return null;
    }

    /**
     * Gets items as ids.
     *
     * @return the items as ids
     */
    public Collection<Long> getItemsAsIds() {
        Collection<Long> itemIds = new HashSet<>();
        getSetOfItems().forEach(item -> {
            itemIds.add(item.getUid());
        });
        return itemIds;
    }

    /**
     * Contains uid boolean.
     *
     * @param id the id
     * @return the boolean
     */
    public boolean containsUid(Long id) {
        return findByUid(id) != null;
    }
}

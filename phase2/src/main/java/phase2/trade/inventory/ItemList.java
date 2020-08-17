package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
public abstract class ItemList {

    // I don't know why the subclasses are complaining about the missing primary key
    // This is a standard implementation according to JPA documentation to have this primary key in a MappedSuperClass
    // There are related questions in stackoverflow but none has come up with a solution

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

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

    public void addItem(Set<Item> items) {
        this.getSetOfItems().addAll(items);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int size() {
        return getSetOfItems().size();
    }

    public void removeItem(Item... items) {
        for (Item item : items) {
            this.getSetOfItems().remove(item);
        }
    }

    public void removeItemByUid(Long... uids) {
        for (Long uid : uids) {
            Item item = findByUid(uid);
            if (item != null) removeItem(item);
        }
    }

    public Item findByUid(Long uid) {
        for (Item item : getSetOfItems()) {
            if (item.getUid().equals(uid)) return item;
        }
        return null;
    }

    public boolean contains(Long uid) {
        return findByUid(uid) != null;
    }
}

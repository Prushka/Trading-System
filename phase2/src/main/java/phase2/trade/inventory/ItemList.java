package phase2.trade.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Item> listOfItems = new ArrayList<>();

    @OneToOne
    private User owner;

    public ItemList(List<Item> listOfItems, User owner, Ownership ownership) {
        this.listOfItems = listOfItems;
        this.owner = owner;
    }

    public ItemList() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Item> getListOfItems() {
        return listOfItems;
    }

    public void setListOfItems(List<Item> inventory) {
        this.listOfItems = inventory;
    }

    public void addItem(Item... items) {
        addItem(Arrays.asList(items));
    }

    public void addItem(List<Item> items) {
        this.listOfItems.addAll(items);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public int size() {
        return listOfItems.size();
    }

    public abstract ItemListType getItemListType();

    public void removeItem(Item... items) {
        for (Item item : items) {
            this.listOfItems.remove(item);
        }
    }

    public void removeItemByUid(Long... uids) {
        for (Long uid : uids) {
            Item item = findByUid(uid);
            if (item != null) removeItem(item);
        }
    }

    public Item get(int index) {
        return getListOfItems().get(index);
    }

    public Item findByUid(Long uid) {
        for (Item item : getListOfItems()) {
            if (item.getUid().equals(uid)) return item;
        }
        return null;
    }
}

package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade = CascadeType.ALL)
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

    public void addItem(Item item) {
        this.listOfItems.add(item);
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

    public abstract InventoryType getInventoryType();

    public void removeItem(Item item) {
        this.listOfItems.remove(item);
    }
}

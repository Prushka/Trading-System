package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Item> itemList;

    @OneToOne
    private User owner;

    public ItemList(List<Item> itemList, User owner, Ownership ownership){
        this.itemList = itemList;
        this.owner = owner;
    }

    public ItemList() {
    }

    public User getOwner() {
        return owner;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> inventory) {
        this.itemList = inventory;
    }

    public void addItem(Item item){
        this.itemList.add(item);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public abstract InventoryType getInventoryType();
}

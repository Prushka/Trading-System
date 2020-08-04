package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.user.User;

import java.util.List;

public class Inventory {

    private List<Item> inventory;

    private User owner;

    private InventoryType inventoryType;

    public Inventory(List<Item> inventory, User owner, Ownership ownership){
        this.inventory = inventory;
        this.owner = owner;
        this.ownership = ownership;
    }

    public User getOwner() {
        return owner;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
}

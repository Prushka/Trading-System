package group;

import java.util.ArrayList;
import java.util.List;

public class ItemInventory {
    private List<Item> allItems;

    public ItemInventory () {
        allItems = new ArrayList<>();
    }

    public ItemInventory (List<Item> allItems) {
        this.allItems = allItems;
    }

    public List<Item> getAllItems() {
        return allItems;
    }

    public void setAllItems(List<Item> allItems) {
        this.allItems = allItems;
    }
}

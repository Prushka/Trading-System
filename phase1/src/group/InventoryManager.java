package group;

public class InventoryManager {
    private static final ItemInventory allItems = new ItemInventory();

    public void add (Item x) {
        allItems.getAllItems().add(x);
        System.out.println("Item has been added to the list of all Items.");
    }

    public void remove (Item x) {
        allItems.getAllItems().remove(x);
        System.out.println("Item has been removed from the list of all items.");
    }
}

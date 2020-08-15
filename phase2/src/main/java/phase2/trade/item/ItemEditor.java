package phase2.trade.item;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusSucceeded;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor {

    private final List<Item> items;

    public ItemEditor(List<Item> items) {
        this.items = items;
    }
    public ItemEditor(Item item) {
        this.items = new ArrayList<>();
        this.items.add(item);
    }

    public void alterWillingness(Willingness willingness, StatusCallback statusCallback) {
        for (Item item : items) {
            // if (item.getOwnership() != Ownership.OWNER) {
            //     statusCallback.call(new StatusFailed("You do not own this product yet!"));
            //     return;
            // }
        }
        items.forEach(item -> item.setWillingness(willingness));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterDescription(String description, StatusCallback statusCallback) {
        items.forEach(item -> item.setDescription(description));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterName(String name, StatusCallback statusCallback) {
        items.forEach(item -> item.setName(name));
        statusCallback.call(new StatusSucceeded());
    }

    public List<Item> getItems() {
        return items;
    }
}

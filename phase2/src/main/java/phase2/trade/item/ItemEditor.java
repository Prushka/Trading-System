package phase2.trade.item;

import javafx.beans.property.BooleanProperty;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
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


    public void alterWillingness(String willingness, StatusCallback statusCallback) {
        alterWillingness(Willingness.valueOf(willingness), statusCallback);
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

    public void alterCategory(String category, StatusCallback statusCallback) {
        items.forEach(item -> item.setCategory(Category.valueOf(category)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterPrice(String price, StatusCallback statusCallback) {
        if (!isNumber(price)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        items.forEach(item -> item.setPrice(Double.parseDouble(price)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterQuantity(String quantity, StatusCallback statusCallback) {
        if (!isInt(quantity)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        items.forEach(item -> item.setQuantity(Integer.parseInt(quantity)));
        statusCallback.call(new StatusSucceeded());
    }

    public List<Item> getItems() {
        return items;
    }

    private boolean isInt(String toExam) {
        if (toExam.matches("[0-9]+")) {
            return true;
        }
        return false;
    }

    private boolean isNumber(String toExam) {
        if (toExam.matches("-?\\d+(\\.\\d+)?")) {
            return true;
        }
        return false;
    }
}

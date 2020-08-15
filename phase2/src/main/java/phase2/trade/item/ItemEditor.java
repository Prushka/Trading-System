package phase2.trade.item;

import javafx.beans.property.BooleanProperty;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.controller.Editor;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionProperty;

import java.util.ArrayList;
import java.util.List;

public class ItemEditor extends Editor<Item> {

    public ItemEditor(List<Item> entities) {
        super(entities);
    }

    public ItemEditor(Item entity) {
        super(entity);
    }

    public void alterWillingness(String willingness, StatusCallback statusCallback) {
        alterWillingness(Willingness.valueOf(willingness), statusCallback);
    }

    public void alterWillingness(Willingness willingness, StatusCallback statusCallback) {
        entities.forEach(item -> item.setWillingness(willingness));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterDescription(String description, StatusCallback statusCallback) {
        entities.forEach(item -> item.setDescription(description));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterName(String name, StatusCallback statusCallback) {
        entities.forEach(item -> item.setName(name));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterCategory(String category, StatusCallback statusCallback) {
        entities.forEach(item -> item.setCategory(Category.valueOf(category)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterOwnership(String ownership, StatusCallback statusCallback) {
        entities.forEach(item -> item.setOwnership(Ownership.valueOf(ownership)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterPrice(String price, StatusCallback statusCallback) {
        if (!isNumber(price)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(item -> item.setPrice(Double.parseDouble(price)));
        statusCallback.call(new StatusSucceeded());
    }

    public void alterQuantity(String quantity, StatusCallback statusCallback) {
        if (!isInt(quantity)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(item -> item.setQuantity(Integer.parseInt(quantity)));
        statusCallback.call(new StatusSucceeded());
    }
}

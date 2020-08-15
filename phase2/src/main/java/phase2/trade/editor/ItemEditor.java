package phase2.trade.editor;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;

import java.util.List;

public class ItemEditor extends Editor<Item> {

    public ItemEditor(List<Item> entities, ConfigBundle configBundle) {
        super(entities,configBundle);
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

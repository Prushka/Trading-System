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

/**
 * The Item editor used to edit {@link Item}s.<p>
 * Used in {@link phase2.trade.item.controller.InventoryController} and {@link phase2.trade.item.controller.ItemManageController}<p>
 * where users can directly edit their items in the TableView.
 *
 * @author Dan Lyu
 * @see Item
 */
public class ItemEditor extends Editor<Item> {

    /**
     * Constructs a new Item editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     */
    public ItemEditor(List<Item> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }


    /**
     * Alter willingness.
     *
     * @param willingness    the willingness
     * @param statusCallback the status callback
     */
    public void alterWillingness(String willingness, StatusCallback statusCallback) {
        alterWillingness(Willingness.valueOf(willingness), statusCallback);
    }

    /**
     * Alter willingness.
     *
     * @param willingness    the willingness
     * @param statusCallback the status callback
     */
    public void alterWillingness(Willingness willingness, StatusCallback statusCallback) {
        entities.forEach(item -> item.setWillingness(willingness));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter description.
     *
     * @param description    the description
     * @param statusCallback the status callback
     */
    public void alterDescription(String description, StatusCallback statusCallback) {
        entities.forEach(item -> item.setDescription(description));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter name.
     *
     * @param name           the name
     * @param statusCallback the status callback
     */
    public void alterName(String name, StatusCallback statusCallback) {
        entities.forEach(item -> item.setName(name));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter category.
     *
     * @param category       the category
     * @param statusCallback the status callback
     */
    public void alterCategory(String category, StatusCallback statusCallback) {
        entities.forEach(item -> item.setCategory(Category.valueOf(category)));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter ownership.
     *
     * @param ownership      the ownership
     * @param statusCallback the status callback
     */
    public void alterOwnership(String ownership, StatusCallback statusCallback) {
        entities.forEach(item -> item.setOwnership(Ownership.valueOf(ownership)));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter price.
     *
     * @param price          the price
     * @param statusCallback the status callback
     */
    public void alterPrice(String price, StatusCallback statusCallback) {
        if (!isNumber(price)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(item -> item.setPrice(Double.parseDouble(price)));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter quantity.
     *
     * @param quantity       the quantity
     * @param statusCallback the status callback
     */
    public void alterQuantity(String quantity, StatusCallback statusCallback) {
        if (!isInt(quantity)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(item -> item.setQuantity(Integer.parseInt(quantity)));
        statusCallback.call(new StatusSucceeded());
    }
}

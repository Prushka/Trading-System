package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.user.User;

/**
 * The User table.
 *
 * @author Dan Lyu
 */
public class UserTable extends AllTable {
    /**
     * The Owner.
     */
    User owner;

    /**
     * Constructs a new User table.
     *
     * @param owner               the owner
     * @param controllerResources the controller resources
     */
    public UserTable(User owner, ControllerResources controllerResources) {
        this(owner, FXCollections.observableArrayList(), controllerResources);
    }

    /**
     * Constructs a new User table.
     *
     * @param owner               the owner
     * @param items               the items
     * @param controllerResources the controller resources
     */
    public UserTable(User owner, ObservableList<Item> items, ControllerResources controllerResources) {
        super(items, controllerResources);
        this.owner = owner;
    }
}

package phase2.trade.trade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.user.User;

public class UserTable extends AllTable {
    User owner;

    public UserTable(User owner, ControllerResources controllerResources) {
        this(owner, FXCollections.observableArrayList(), controllerResources);
    }

    public UserTable(User owner, ObservableList<Item> items, ControllerResources controllerResources) {
        super(items, controllerResources);
        this.owner = owner;
    }
}

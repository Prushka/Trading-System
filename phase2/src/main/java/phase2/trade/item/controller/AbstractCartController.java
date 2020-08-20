package phase2.trade.item.controller;

import javafx.beans.property.SimpleStringProperty;
import phase2.trade.controller.ControllerResources;

public abstract class AbstractCartController extends ItemController {

    public AbstractCartController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable);
    }

    @Override
    protected void addQuantityColumn(boolean editable) {
        tableViewGenerator.addColumn("Quantity", param ->
                new SimpleStringProperty(String.valueOf(getAccountManager().getLoggedInUser().getCart().findQuantity(param.getValue()))));
    }
}

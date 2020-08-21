package phase2.trade.item.controller;

import javafx.beans.property.SimpleStringProperty;
import phase2.trade.controller.ControllerResources;

/**
 * The base cart controller.
 *
 * @author Dan Lyu
 * @see phase2.trade.trade.controller.TradeUserTableController
 * @see CartController
 */
public abstract class AbstractCartController extends ItemController {

    /**
     * Constructs a new Abstract cart controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     * @param ifEditable          the if editable
     */
    public AbstractCartController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable);
    }

    @Override
    protected void addQuantityColumn(boolean editable) {
        tableViewGenerator.addColumn("Quantity", param ->
                new SimpleStringProperty(String.valueOf(getAccountManager().getLoggedInUser().getCart().findQuantity(param.getValue()))));
    }
}

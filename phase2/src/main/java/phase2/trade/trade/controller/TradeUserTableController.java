package phase2.trade.trade.controller;

import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.item.controller.AbstractCartController;
import phase2.trade.view.TableViewGenerator;

/**
 * The Trade user table controller.
 *
 * @author Dan Lyu
 */
public class TradeUserTableController extends AbstractCartController {

    /**
     * Constructs a new Trade user table controller.
     *
     * @param controllerResources the controller resources
     * @param tableViewGenerator  the table view generator
     */
    public TradeUserTableController(ControllerResources controllerResources, TableViewGenerator<Item> tableViewGenerator) {
        super(controllerResources, true, false);

        this.tableViewGenerator = tableViewGenerator;

        this.tableView = tableViewGenerator.getTableView();

        addNameColumn(true);
        addQuantityColumn(true);
        addPriceColumn(true);
        addOwnerColumn();
        addWillingnessColumn(true);
        tableViewGenerator.build();
    }

}

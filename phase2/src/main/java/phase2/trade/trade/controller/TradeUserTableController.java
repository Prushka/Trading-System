package phase2.trade.trade.controller;

import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.item.controller.ItemController;
import phase2.trade.view.TableViewGenerator;

public class TradeUserTableController extends ItemController {

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

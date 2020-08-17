package phase2.trade.item.controller;

import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.view.TableViewGenerator;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TargetUserTableController extends ItemController {

    public TargetUserTableController(ControllerResources controllerResources, TableViewGenerator<Item> tableViewGenerator) {
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

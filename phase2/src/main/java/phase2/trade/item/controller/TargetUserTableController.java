package phase2.trade.item.controller;

import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.view.TableViewGenerator;

public class TargetUserTableController extends ItemController {

    public TargetUserTableController(ControllerResources controllerResources, TableViewGenerator<Item> tableViewGenerator) {
        super(controllerResources, true, false);
        this.tableViewGenerator = tableViewGenerator;
        this.tableView = tableViewGenerator.getTableView();

        addNameColumn(false);
        addDescriptionColumn(false);
        addWillingnessColumn(false);
        addCategoryColumn(false);
        tableViewGenerator.build();
    }

}

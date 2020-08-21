package phase2.trade.trade.controller;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.view.TableViewGenerator;

/**
 * The All table.
 *
 * @author Dan Lyu
 */
public class AllTable extends AbstractController {
    /**
     * The Table view generator.
     */
    TableViewGenerator<Item> tableViewGenerator;
    /**
     * The Trade user table controller.
     */
    TradeUserTableController tradeUserTableController;

    /**
     * Constructs a new All table.
     *
     * @param items               the items
     * @param controllerResources the controller resources
     */
    public AllTable(ObservableList<Item> items, ControllerResources controllerResources) {
        super(controllerResources);
        tableViewGenerator = new TableViewGenerator<>(items);
        tableViewGenerator.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewGenerator.getTableView().setPrefWidth(650);
        tradeUserTableController = new TradeUserTableController(getControllerResources(), tableViewGenerator);
        tableViewGenerator.getTableView().setItems(items);
    }

    /**
     * Gets table view.
     *
     * @return the table view
     */
    public TableView<Item> getTableView() {
        return tableViewGenerator.getTableView();
    }
}
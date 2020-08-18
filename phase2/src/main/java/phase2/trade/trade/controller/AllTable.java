package phase2.trade.trade.controller;

import javafx.collections.ObservableList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.view.TableViewGenerator;

public class AllTable extends AbstractController {
    TableViewGenerator<Item> tableViewGenerator;
    TradeUserTableController tradeUserTableController;

    public AllTable(ObservableList<Item> items, ControllerResources controllerResources) {
        super(controllerResources);
        tableViewGenerator = new TableViewGenerator<>(items);
        tableViewGenerator.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableViewGenerator.getTableView().setPrefWidth(650);
        tradeUserTableController = new TradeUserTableController(getControllerResources(), tableViewGenerator);
        tableViewGenerator.getTableView().setItems(items);
    }

    public TableView<Item> getTableView() {
        return tableViewGenerator.getTableView();
    }
}
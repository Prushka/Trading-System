package phase2.trade.presenter;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import phase2.trade.item.Item;

import java.util.ArrayList;
import java.util.List;


public class TableViewGenerator<T> {

    private final ObservableList<T> data;

    private final TableView<T> tableView = new TableView<>();

    private final List<TableColumn<T, String>> listOfColumns = new ArrayList<>();

    private int defaultMinWidth = 150;

    public TableViewGenerator(ObservableList<T> data, int defaultMinWidth) {
        this.data = data;
        this.defaultMinWidth = defaultMinWidth;
    }

    private TableColumn<T, String> getTableColumn(String name, String fieldName, int minWidth) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setMinWidth(minWidth);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    public TableViewGenerator<T> addColumn(String name, String fieldName, int minWidth) {
        listOfColumns.add(getTableColumn(name, fieldName, minWidth));
        return this;
    }

    public TableViewGenerator<T> addColumn(String name, String fieldName) {
        listOfColumns.add(getTableColumn(name, fieldName, defaultMinWidth));
        return this;
    }

    public TableView<T> build() {
        tableView.setItems(data);
        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }
}

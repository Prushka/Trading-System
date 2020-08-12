package phase2.trade.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import phase2.trade.item.Item;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TableViewGenerator<T> {

    private final ObservableList<T> original;

    private final TableView<T> tableView;

    private final List<TableColumn<T, String>> listOfColumns = new ArrayList<>();

    private int defaultMinWidth;

    private final FilteredList<T> filteredList;

    private final FilterGroup<T> filterGroup;

    public TableViewGenerator(ObservableList<T> original, int defaultMinWidth, TableView<T> tableView) {
        this.original = original;
        this.defaultMinWidth = defaultMinWidth;
        filteredList = new FilteredList<>(original, p -> true);
        this.tableView = tableView;
        this.filterGroup = new FilterGroup<>(filteredList);
    }

    public TableViewGenerator(ObservableList<T> original, int defaultMinWidth) {
        this(original, defaultMinWidth, new TableView<>());
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

    public TableViewGenerator<T> addColumn(String name,
                                           Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> cellValueFactory) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cellValueFactory);
        listOfColumns.add(column);
        return this;
    }

    public TableView<T> build() {
        filterGroup.group();
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }

    public FilterGroup<T> getFilterGroup() {
        return filterGroup;
    }
}

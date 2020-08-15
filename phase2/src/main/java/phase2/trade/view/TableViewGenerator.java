package phase2.trade.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.text.Text;
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

    public TableViewGenerator(ObservableList<T> original, TableView<T> tableView) {
        this.original = original;
        filteredList = new FilteredList<>(original, p -> true);
        this.tableView = tableView;
        this.filterGroup = new FilterGroup<>(filteredList);
    }

    public TableViewGenerator(ObservableList<T> original) {
        this(original, new TableView<>());
    }

    private TableColumn<T, String> getTableColumn(String name, String fieldName) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    public TableViewGenerator<T> addColumn(String name, String fieldName, int prefWidth) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setPrefWidth(prefWidth);

        return this;
    }

    public TableViewGenerator<T> addColumnEditable(String name, String fieldName, int prefWidth, EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setPrefWidth(prefWidth);

        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    public TableViewGenerator<T> addColumnEditable(String name, String fieldName, EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    public TableViewGenerator<T> addColumnEditable(String name, String fieldName,
                                                   EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit,
                                                   Callback<TableColumn<T, String>, TableCell<T, String>> cellFactory) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setCellFactory(cellFactory);
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    public TableViewGenerator<T> addColumn(String name, String fieldName) {
        listOfColumns.add(getTableColumn(name, fieldName));
        return this;
    }

    public TableViewGenerator<T> addColumn(String name,
                                           Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> cellValueFactory) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cellValueFactory);
        listOfColumns.add(column);
        return this;
    }

    // https://stackoverflow.com/questions/14650787/javafx-column-in-tableview-auto-fit-size
    public void autoResizeColumns() { // well it seems that this is done by default
        //Set the right policy
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().forEach((column) ->
        {
            //Minimal width = columnheader
            Text t = new Text(column.getText());
            double max = t.getLayoutBounds().getWidth();
            for (int i = 0; i < tableView.getItems().size(); i++) {
                //cell must not be empty
                if (column.getCellData(i) != null) {
                    t = new Text(column.getCellData(i).toString());
                    double calcwidth = t.getLayoutBounds().getWidth();
                    //remember new max-width
                    if (calcwidth > max) {
                        max = calcwidth;
                    }
                }
            }
            //set the new max-width with some extra space
            column.setPrefWidth(max + 10.0d);
        });
    }

    public TableView<T> build() {
        filterGroup.group();
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        //autoResizeColumns();
        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }

    public FilterGroup<T> getFilterGroup() {
        return filterGroup;
    }
}

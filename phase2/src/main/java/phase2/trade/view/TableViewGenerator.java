package phase2.trade.view;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import phase2.trade.modified.JFXComboBoxTableCell;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * The Table view generator.
 *
 * @param <T> the entity type of the TableView
 * @author Dan Lyu
 */
public class TableViewGenerator<T> {

    private final TableView<T> tableView;

    private final List<TableColumn<T, String>> listOfColumns = new ArrayList<>();

    private final FilteredList<T> filteredList;

    private SortedList<T> sortedList;

    private final FilterGroup<T> filterGroup;

    /**
     * Constructs a new Table view generator.
     *
     * @param original the original
     */
    public TableViewGenerator(ObservableList<T> original) {
        this(original, new TableView<>());
    }

    /**
     * Constructs a new Table view generator.
     *
     * @param original the original
     */
    public TableViewGenerator(Collection<T> original) {
        this(FXCollections.observableArrayList(original), new TableView<>());
    }

    /**
     * Constructs a new Table view generator.
     *
     * @param original  the original
     * @param tableView the table view
     */
    public TableViewGenerator(ObservableList<T> original, TableView<T> tableView) {
        filteredList = new FilteredList<>(original, p -> true);
        this.tableView = tableView;
        this.filterGroup = new FilterGroup<>(filteredList);
    }

    private TableColumn<T, String> getTableColumn(String name, String fieldName) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    /**
     * Add column table view generator.
     *
     * @param name      the name
     * @param fieldName the field name
     * @param prefWidth the pref width
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumn(String name, String fieldName, int prefWidth) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setPrefWidth(prefWidth);

        return this;
    }

    /**
     * Add column editable table view generator.
     *
     * @param name         the name
     * @param fieldName    the field name
     * @param prefWidth    the pref width
     * @param onEditCommit the on edit commit
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumnEditable(String name, String fieldName, int prefWidth, EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setPrefWidth(prefWidth);

        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    /**
     * Add column editable table view generator.
     *
     * @param name         the name
     * @param fieldName    the field name
     * @param onEditCommit the on edit commit
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumnEditable(String name, String fieldName, EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    /**
     * Add column editable table view generator.
     *
     * @param name         the name
     * @param fieldName    the field name
     * @param onEditCommit the on edit commit
     * @param cellFactory  the cell factory
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumnEditable(String name, String fieldName,
                                                   EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit,
                                                   Callback<TableColumn<T, String>,
                                                           TableCell<T, String>> cellFactory) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setCellFactory(cellFactory);
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    /**
     * Add column editable table view generator.
     *
     * @param name         the name
     * @param fieldName    the field name
     * @param onEditCommit the on edit commit
     * @param comboBox     the combo box
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumnEditable(String name, String fieldName,
                                                   EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit,
                                                   ObservableList<String> comboBox) {
        TableColumn<T, String> column = getTableColumn(name, fieldName);
        listOfColumns.add(column);
        column.setCellFactory(JFXComboBoxTableCell.forTableColumn(comboBox));
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    /**
     * Add column table view generator.
     *
     * @param name      the name
     * @param fieldName the field name
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumn(String name, String fieldName) {
        listOfColumns.add(getTableColumn(name, fieldName));
        return this;
    }

    /**
     * Add column table view generator.
     *
     * @param name             the name
     * @param cellValueFactory the cell value factory
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumn(String name,
                                           Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> cellValueFactory) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cellValueFactory);
        listOfColumns.add(column);
        return this;
    }

    /**
     * Add column editable table view generator.
     *
     * @param name             the name
     * @param cellValueFactory the cell value factory
     * @param onEditCommit     the on edit commit
     * @return the table view generator
     */
    public TableViewGenerator<T> addColumnEditable(String name,
                                                   Callback<TableColumn.CellDataFeatures<T, String>, ObservableValue<String>> cellValueFactory,
                                                   EventHandler<TableColumn.CellEditEvent<T, String>> onEditCommit) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setCellValueFactory(cellValueFactory);
        listOfColumns.add(column);
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(onEditCommit);
        return this;
    }

    /**
     * Build table view.
     *
     * @return the table view
     */
    public TableView<T> build() {
        filterGroup.group();
        sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);
        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }

    /**
     * Gets sorted list.
     *
     * @return the sorted list
     */
    public SortedList<T> getSortedList() {
        return sortedList;
    }

    /**
     * Gets filter group.
     *
     * @return the filter group
     */
    public FilterGroup<T> getFilterGroup() {
        return filterGroup;
    }

    /**
     * Gets table view.
     *
     * @return the table view
     */
    public TableView<T> getTableView() {
        return tableView;
    }
}

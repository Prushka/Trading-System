package phase2.trade.view;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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

    public TableViewGenerator(ObservableList<T> original, int defaultMinWidth, TableView<T> tableView) {
        this.original = original;
        this.defaultMinWidth = defaultMinWidth;
        filteredList = new FilteredList<>(original, p -> true);
        this.tableView = tableView;
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


    public TableViewGenerator<T> addSearch(TextField textField, FilterPredicate<T, String> filterPredicate) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return filterPredicate.test(t, textField.getText());
            });
        });
        return this;
    }

    public <E> TableViewGenerator<T> addComboBox(ComboBox<E> comboBox, FilterPredicate<T, E> filterPredicate) {
        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(t -> {
                if (newValue == null) {
                    return true;
                }
                return filterPredicate.test(t, comboBox.getValue());
            });
        });
        return this;
    }

    private Map<CheckBox, FilterPredicate<T, Boolean>> checkBoxGroup = new HashMap<>();

    public TableViewGenerator<T> addCheckBox(CheckBox checkBox, FilterPredicate<T, Boolean> filterPredicate) {
        checkBoxGroup.put(checkBox,filterPredicate);
        return this;
    }

    public TableViewGenerator<T> groupCheckBoxes() {
        for(Map.Entry<CheckBox, FilterPredicate<T,Boolean>> entry : checkBoxGroup.entrySet()) {
            entry.getKey().selectedProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(t -> {
                    if (newValue == null) {
                        return true;
                    }
                    boolean result = false;
                    for(Map.Entry<CheckBox, FilterPredicate<T,Boolean>> entry2 : checkBoxGroup.entrySet()) {
                        if(entry2.getKey().isSelected())
                        result = entry2.getValue().test(t, entry2.getKey().isSelected()) || result;
                    }
                    return result;
                });
            });
        }
        return this;
    }

    public TableView<T> build() {
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }
}

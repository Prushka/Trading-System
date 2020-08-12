package phase2.trade.view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        textFieldGroup.put(textField, filterPredicate);
        return this;
    }

    public TableViewGenerator<T> addComboBox(ComboBox<String> comboBox, FilterPredicate<T, String> filterPredicate) {
        comboBoxGroup.put(comboBox, filterPredicate);
        return this;
    }

    // well java doesn't allow declaring generic type in the field, so the current implementation only allows ComboBox<String>

    private Map<ComboBox<String>, FilterPredicate<T, String>> comboBoxGroup = new HashMap<>();

    private Map<CheckBox, FilterPredicate<T, Boolean>> checkBoxGroup = new HashMap<>();

    private Map<TextField, FilterPredicate<T, String>> textFieldGroup = new HashMap<>();


    public TableViewGenerator<T> addCheckBox(CheckBox checkBox, FilterPredicate<T, Boolean> filterPredicate) {
        checkBoxGroup.put(checkBox, filterPredicate);
        return this;
    }

    public TableViewGenerator<T> group() {
        for (Map.Entry<CheckBox, FilterPredicate<T, Boolean>> entry : checkBoxGroup.entrySet()) {
            entry.getKey().selectedProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(this::bind);
            });
        }
        for (Map.Entry<ComboBox<String>, FilterPredicate<T, String>> comboBox : comboBoxGroup.entrySet()) {
            comboBox.getKey().valueProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(this::bind);
            });
        }
        for (Map.Entry<TextField, FilterPredicate<T, String>> textField : textFieldGroup.entrySet()) {
            textField.getKey().textProperty().addListener((observable, oldValue, newValue) -> {
                System.out.println("123");
                filteredList.setPredicate(this::bind);
            });
        }
        return this;
    }

    private Boolean bind(T entity) {
        boolean result = true;
        for (Map.Entry<CheckBox, FilterPredicate<T, Boolean>> checkBox : checkBoxGroup.entrySet()) {
            if (!checkBox.getKey().isSelected()) {
                result = result && !checkBox.getValue().test(entity, checkBox.getKey().isSelected());
            }
        }
        for (Map.Entry<ComboBox<String>, FilterPredicate<T, String>> comboBox : comboBoxGroup.entrySet()) {
            if (comboBox.getKey().valueProperty() == null || comboBox.getKey().valueProperty().get() == null ||
                    comboBox.getKey().valueProperty().get().equalsIgnoreCase("all")) {
                // result = true || result;
            } else {
                result = comboBox.getValue().test(entity, comboBox.getKey().valueProperty().get()) && result;
            }
        }
        for (Map.Entry<TextField, FilterPredicate<T, String>> textField : textFieldGroup.entrySet()) {
            if (textField.getKey().getText() == null || textField.getKey().getText().isEmpty()) {
                // result = true || result;
            } else {
                result = textField.getValue().test(entity, textField.getKey().getText()) && result;
            }
        }
        return result;
    }

    public TableView<T> build() {
        SortedList<T> sortedList = new SortedList<>(filteredList);
        sortedList.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortedList);

        tableView.getColumns().addAll(listOfColumns);
        return tableView;
    }
}

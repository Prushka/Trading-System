package phase2.trade.view;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.HashMap;
import java.util.Map;

public class FilterGroup<T> {

    // well java doesn't allow declaring generic type in the field, so the current implementation only allows ComboBox<String>
    // even though I implement another class for the matter, the class still need to accept something from the outside world

    private final FilteredList<T> filteredList;

    private Map<ComboBox<String>, FilterPredicate<T, String>> comboBoxGroup = new HashMap<>();

    private Map<CheckBox, FilterPredicate<T, Boolean>> checkBoxGroup = new HashMap<>();

    private Map<TextField, FilterPredicate<T, String>> textFieldGroup = new HashMap<>();

    public FilterGroup(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    public FilterGroup<T> addSearch(TextField textField, FilterPredicate<T, String> filterPredicate) {
        textFieldGroup.put(textField, filterPredicate);
        return this;
    }

    public FilterGroup<T> addComboBox(ComboBox<String> comboBox, FilterPredicate<T, String> filterPredicate) {
        comboBoxGroup.put(comboBox, filterPredicate);
        return this;
    }

    public FilterGroup<T> addCheckBox(CheckBox checkBox, FilterPredicate<T, Boolean> filterPredicate) {
        checkBoxGroup.put(checkBox, filterPredicate);
        return this;
    }


    public FilterGroup<T> group() {
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
}

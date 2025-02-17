package phase2.trade.view;

import javafx.collections.transformation.FilteredList;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

import java.util.HashMap;
import java.util.Map;

/**
 * The Filter group used to bind a bunch of {@link FilterPredicate} and view nodes that work towards one FilteredList.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
public class FilterGroup<T> {

    // well java doesn't allow declaring generic type in the field, so the current implementation only allows ComboBox<String>
    // even though I implement another class for the matter, the class still need to accept something from the outside world

    private final FilteredList<T> filteredList;

    private final Map<ComboBox<String>, FilterPredicate<T, String>> comboBoxGroup = new HashMap<>();

    private final Map<CheckBox, FilterPredicate<T, Boolean>> checkBoxGroup = new HashMap<>();

    private final Map<TextField, FilterPredicate<T, String>> textFieldGroup = new HashMap<>();

    private final Map<ToggleButton, FilterPredicate<T, Boolean>> toggleGroup = new HashMap<>();


    /**
     * Constructs a new Filter group.
     *
     * @param filteredList the filtered list
     */
    public FilterGroup(FilteredList<T> filteredList) {
        this.filteredList = filteredList;
    }

    /**
     * Add search filter group.
     *
     * @param textField       the text field
     * @param filterPredicate the filter predicate
     * @return the filter group
     */
    public FilterGroup<T> addSearch(TextField textField, FilterPredicate<T, String> filterPredicate) {
        textFieldGroup.put(textField, filterPredicate);
        return this;
    }

    /**
     * Add combo box filter group.
     *
     * @param comboBox        the combo box
     * @param filterPredicate the filter predicate
     * @return the filter group
     */
    public FilterGroup<T> addComboBox(ComboBox<String> comboBox, FilterPredicate<T, String> filterPredicate) {
        comboBoxGroup.put(comboBox, filterPredicate);
        return this;
    }

    /**
     * Add check box filter group.
     *
     * @param checkBox        the check box
     * @param filterPredicate the filter predicate
     * @return the filter group
     */
    public FilterGroup<T> addCheckBox(CheckBox checkBox, FilterPredicate<T, Boolean> filterPredicate) {
        checkBoxGroup.put(checkBox, filterPredicate);
        return this;
    }

    /**
     * Add toggle button filter group.
     *
     * @param toggleButton    the toggle button
     * @param filterPredicate the filter predicate
     * @return the filter group
     */
    public FilterGroup<T> addToggleButton(ToggleButton toggleButton, FilterPredicate<T, Boolean> filterPredicate) {
        toggleGroup.put(toggleButton, filterPredicate);
        return this;
    }


    /**
     * Group filter group.
     *
     * @return the filter group
     */
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
                filteredList.setPredicate(this::bind);
            });
        }
        for (Map.Entry<ToggleButton, FilterPredicate<T, Boolean>>  toggleButton: toggleGroup.entrySet()) {
            toggleButton.getKey().selectedProperty().addListener((observable, oldValue, newValue) -> {
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
        for (Map.Entry<ToggleButton, FilterPredicate<T, Boolean>> toggleButton : toggleGroup.entrySet()) {
            if (!toggleButton.getKey().isSelected()) {
                result = result && !toggleButton.getValue().test(entity, toggleButton.getKey().isSelected());
            }
        }
        return result;
    }
}

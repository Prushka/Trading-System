package phase2.trade.view;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import phase2.trade.item.Category;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.stream.Stream;

public class NodeFactory {

    public NodeFactory() {

    }

    public TextField getDefaultTextField(String promptText) {
        JFXTextField jfxTextField = new JFXTextField();
        jfxTextField.setPromptText(promptText);
        jfxTextField.setLabelFloat(true);
        return jfxTextField;
    }

    public RadioButton getDefaultRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radio = new JFXRadioButton(text);
        radio.setToggleGroup(toggleGroup);
        return radio;
    }

    public enum ComboBoxType {
        Category
    }

    public ComboBox<String> getComboBox(ComboBoxType type) {
        switch (type) {
            case Category:
                return new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))));
            default:
                return null;
        }
    }

    public ObservableList<String> getEnumAsObservableString(Class<? extends Enum<?>> clazz) {
        return FXCollections.observableArrayList(Arrays.asList(Stream.of(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }
}

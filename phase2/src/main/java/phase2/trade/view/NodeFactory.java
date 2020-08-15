package phase2.trade.view;


import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import phase2.trade.item.Category;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
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

    public TextArea getDefaultTextArea(String promptText) {
        JFXTextArea area = new JFXTextArea();
        area.setPromptText(promptText);
        area.setLabelFloat(true);
        return area;
    }

    public RadioButton getDefaultRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radio = new JFXRadioButton(text);
        radio.setToggleGroup(toggleGroup);
        return radio;
    }

    public enum ComboBoxType {
        Category
    }

    public ComboBox<String> getComboBox(Class<? extends Enum<?>> clazz) {
        return new JFXComboBox<>(getEnumAsObservableString(clazz));
    }

    public ComboBox<String> getComboBox(Collection<String> list) {
        return new JFXComboBox<>(FXCollections.observableArrayList(list));
    }

    public ComboBox<String> getComboBox() {
        return new JFXComboBox<>();
    }

    public ObservableList<String> getEnumAsObservableString(Class<? extends Enum<?>> clazz) {
        return FXCollections.observableArrayList(Arrays.asList(Stream.of(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }
}

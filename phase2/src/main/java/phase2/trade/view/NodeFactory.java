package phase2.trade.view;


import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
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

    public Button getDefaultRaisedButton(String text) {
        JFXButton button = new JFXButton(text);
        button.setDisableVisualFocus(true);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        return button;
    }


    public Button getDefaultRaisedButton(String text, String style) {
        Button button = getDefaultRaisedButton(text);
        button.setStyle(style);
        return button;
    }

    public Button getDefaultFlatButton(String text) {
        JFXButton button = new JFXButton(text);
        button.setDisableVisualFocus(true);
        return button;
    }

    public RadioButton getDefaultRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radio = new JFXRadioButton(text);
        radio.setToggleGroup(toggleGroup);
        return radio;
    }

    public enum ComboBoxType {
        Category
    }

    public JFXComboBox<String> getComboBoxByType(ComboBoxType type){
        switch (type) {
            case Category:
                JFXComboBox<String> comboBox = getComboBox(Category.class);
                comboBox.setPromptText("Category");
                comboBox.setLabelFloat(true);
                comboBox.getItems().add("ALL");
                return comboBox;
        }
        return null;
    }

    public JFXComboBox<String> getComboBox(Class<? extends Enum<?>> clazz) {
        return new JFXComboBox<>(getEnumAsObservableString(clazz));
    }

    public JFXComboBox<String> getComboBox(Collection<String> list) {
        return new JFXComboBox<>(FXCollections.observableArrayList(list));
    }

    public JFXComboBox<String> getComboBox() {
        return new JFXComboBox<>();
    }

    public ObservableList<String> getEnumAsObservableString(Class<? extends Enum<?>> clazz) {
        return FXCollections.observableArrayList(Arrays.asList(Stream.of(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }
}

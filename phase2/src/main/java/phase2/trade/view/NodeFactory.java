package phase2.trade.view;


import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import phase2.trade.config.GeoConfig;
import phase2.trade.database.TriConsumer;
import phase2.trade.item.Category;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class NodeFactory {

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

    public Button getDefaultFlatButton(String text) {
        JFXButton button = new JFXButton(text);
        button.setDisableVisualFocus(true);
        return button;
    }

    public Button getDefaultRaisedButton(String text, String styleClass) {
        Button button = getDefaultRaisedButton(text);
        button.getStyleClass().addAll(styleClass);
        return button;
    }

    public Button getDefaultFlatButton(String text, String styleClass) {
        Button button = getDefaultFlatButton(text);
        button.getStyleClass().addAll(styleClass);
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

    public JFXComboBox<String> getComboBoxByType(ComboBoxType type) {
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

    public void getAddressComboBoxes(TriConsumer<ComboBox<String>, ComboBox<String>, ComboBox<String>> consumer, GeoConfig geoConfig) {
        JFXComboBox<String> countryCombo = getComboBox(geoConfig.getMap().keySet());
        countryCombo.setPromptText("Country");
        countryCombo.setLabelFloat(true);
        JFXComboBox<String> provinceCombo = getComboBox();
        provinceCombo.setPromptText("Province");
        provinceCombo.setLabelFloat(true);
        JFXComboBox<String> cityCombo = getComboBox();
        cityCombo.setPromptText("City");
        cityCombo.setLabelFloat(true);
        countryCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                provinceCombo.setItems(geoConfig.getProvincesByCountry(selectedCountry));
            }
        });

        provinceCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            String selectedProvince = provinceCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                cityCombo.setItems(geoConfig.getCitiesByProvinceCountry(selectedCountry, selectedProvince));
            }
        });
        consumer.consume(countryCombo, provinceCombo, cityCombo);
    }

    public JFXComboBox<String> getComboBox(Class<? extends Enum<?>> clazz) {
        return new JFXComboBox<>(getEnumAsObservableString(clazz));
    }

    public <T> JFXComboBox<T> getComboBox(Collection<T> list) {
        JFXComboBox<T> comboBox = new JFXComboBox<>(FXCollections.observableArrayList(list));
        comboBox.getStyleClass().addAll("default-combo-box");
        return comboBox;
    }

    public JFXComboBox<String> getComboBox() {
        JFXComboBox<String> comboBox = new JFXComboBox<>();
        comboBox.getStyleClass().addAll("default-combo-box");
        return comboBox;
    }

    public ObservableList<String> getEnumAsObservableString(Class<? extends Enum<?>> clazz) {
        return FXCollections.observableArrayList(Arrays.asList(Stream.of(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }
}

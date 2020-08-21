package phase2.trade.view;


import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import phase2.trade.config.GeoConfig;
import phase2.trade.database.TriConsumer;
import phase2.trade.item.Category;
import phase2.trade.permission.PermissionGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

/**
 * The Node factory.
 *
 * @author Dan Lyu
 */
public class NodeFactory {

    /**
     * Gets default text field.
     *
     * @param promptText the prompt text
     * @return the default text field
     */
    public TextField getDefaultTextField(String promptText) {
        JFXTextField jfxTextField = new JFXTextField();
        jfxTextField.setPromptText(promptText);
        jfxTextField.setLabelFloat(true);
        return jfxTextField;
    }

    /**
     * Gets default password text field.
     *
     * @param promptText the prompt text
     * @return the default password text field
     */
    public TextField getDefaultPasswordTextField(String promptText) {
        JFXPasswordField jfxTextField = new JFXPasswordField();
        jfxTextField.setPromptText(promptText);
        jfxTextField.setLabelFloat(true);
        return jfxTextField;
    }

    /**
     * Gets default text area.
     *
     * @param promptText the prompt text
     * @return the default text area
     */
    public TextArea getDefaultTextArea(String promptText) {
        JFXTextArea area = new JFXTextArea();
        area.setPromptText(promptText);
        area.setLabelFloat(true);
        return area;
    }

    /**
     * Gets default raised button.
     *
     * @param text the text
     * @return the default raised button
     */
    public Button getDefaultRaisedButton(String text) {
        JFXButton button = new JFXButton(text);
        button.setDisableVisualFocus(true);
        button.setButtonType(JFXButton.ButtonType.RAISED);
        return button;
    }

    /**
     * Gets default flat button.
     *
     * @param text the text
     * @return the default flat button
     */
    public Button getDefaultFlatButton(String text) {
        JFXButton button = new JFXButton(text);
        button.setDisableVisualFocus(true);
        return button;
    }

    /**
     * Gets default raised button.
     *
     * @param text       the text
     * @param styleClass the style class
     * @return the default raised button
     */
    public Button getDefaultRaisedButton(String text, String styleClass) {
        Button button = getDefaultRaisedButton(text);
        button.getStyleClass().addAll(styleClass);
        return button;
    }

    /**
     * Gets default flat button.
     *
     * @param text       the text
     * @param styleClass the style class
     * @return the default flat button
     */
    public Button getDefaultFlatButton(String text, String styleClass) {
        Button button = getDefaultFlatButton(text);
        button.getStyleClass().addAll(styleClass);
        return button;
    }


    /**
     * Gets default radio button.
     *
     * @param text        the text
     * @param toggleGroup the toggle group
     * @return the default radio button
     */
    public RadioButton getDefaultRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radio = new JFXRadioButton(text);
        radio.setToggleGroup(toggleGroup);
        return radio;
    }

    /**
     * The enum Combo box type.
     *
     * @author Dan Lyu
     */
    public enum ComboBoxType {
        /**
         * Category combo box type.
         */
        Category
    }

    /**
     * Gets combo box by type.
     *
     * @param type    the type
     * @param allText the all text
     * @return the combo box by type
     */
    public JFXComboBox<String> getComboBoxByType(ComboBoxType type, String allText) {
        switch (type) {
            case Category:
                JFXComboBox<String> comboBox = getComboBox(Category.class);
                comboBox.setPromptText("Category");
                comboBox.setLabelFloat(true);
                if (allText != null && !allText.isEmpty()) {
                    comboBox.getItems().add(allText);
                }
                return comboBox;
        }
        return null;
    }

    /**
     * Gets address combo boxes.
     *
     * @param consumer  the consumer
     * @param geoConfig the geo config
     */
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

    /**
     * Gets combo box.
     *
     * @param clazz the clazz
     * @return the combo box
     */
    public JFXComboBox<String> getComboBox(Class<? extends Enum<?>> clazz) {
        return new JFXComboBox<>(getEnumAsObservableString(clazz));
    }

    /**
     * Gets combo box.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the combo box
     */
    public <T> JFXComboBox<T> getComboBox(Collection<T> list) {
        return new JFXComboBox<>(FXCollections.observableArrayList(list));
    }

    /**
     * Gets default combo box.
     *
     * @param <T>  the type parameter
     * @param list the list
     * @return the default combo box
     */
    public <T> JFXComboBox<T> getDefaultComboBox(Collection<T> list) {
        JFXComboBox<T> comboBox = getComboBox(list);
        comboBox.getStyleClass().addAll("default-combo-box");
        return comboBox;
    }

    /**
     * Gets combo box.
     *
     * @return the combo box
     */
    public JFXComboBox<String> getComboBox() {
        return new JFXComboBox<>();
    }

    /**
     * Gets default combo box.
     *
     * @return the default combo box
     */
    public ComboBox<String> getDefaultComboBox() {
        return processDefaultComboBox(getComboBox());
    }

    /**
     * Gets default combo box.
     *
     * @param permissionGroupClass the permission group class
     * @return the default combo box
     */
    public ComboBox<String> getDefaultComboBox(Class<PermissionGroup> permissionGroupClass) {
        JFXComboBox<String> comboBox = getComboBox(permissionGroupClass);
        return processDefaultComboBox(comboBox);
    }

    private <T> ComboBox<T> processDefaultComboBox(JFXComboBox<T> comboBox) {
        comboBox.getStyleClass().addAll("default-combo-box");
        comboBox.setLabelFloat(true);
        return comboBox;
    }

    /**
     * Gets slider.
     *
     * @param min          the min
     * @param max          the max
     * @param defaultValue the default value
     * @return the slider
     */
    public Slider getSlider(int min, int max, int defaultValue) {
        JFXSlider jfxSlider = new JFXSlider();
        jfxSlider.setMin(min);
        jfxSlider.setMax(max);
        jfxSlider.setValue(defaultValue);
        jfxSlider.setIndicatorPosition(JFXSlider.IndicatorPosition.RIGHT);
        return jfxSlider;
    }

    /**
     * Gets enum as observable string.
     *
     * @param clazz the clazz
     * @return the enum as observable string
     */
    public ObservableList<String> getEnumAsObservableString(Class<? extends Enum<?>> clazz) {
        return FXCollections.observableArrayList(Arrays.asList(Stream.of(clazz.getEnumConstants()).map(Enum::name).toArray(String[]::new)));
    }
}

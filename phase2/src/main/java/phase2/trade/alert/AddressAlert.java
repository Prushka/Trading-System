package phase2.trade.alert;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import phase2.trade.address.Address;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;

/**
 * A controller that wraps an Alert used to edit an Address.
 * Contains Country, Province, City, Address Line 1, Address Line2 and Postal Code.
 * The last three fields are optional to be set.
 * This AddressAlert is used to edit a User's Address and a trade's Address.
 *
 * @author Dan Lyu
 * @see phase2.trade.config.GeoConfig
 */
public class AddressAlert extends AbstractController {

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    private final TextField addressLine1 = getNodeFactory().getDefaultTextField("Address Line 1 (Optional)");
    private final TextField addressLine2 = getNodeFactory().getDefaultTextField("Address Line 2 (Optional)");
    private TextField postalCode = getNodeFactory().getDefaultTextField("Postal Code (Optional)");

    private final VBoxAlert addressAlert;

    /**
     * Constructs a new Address alert.
     *
     * @param controllerResources the controller resources
     */
    public AddressAlert(ControllerResources controllerResources) {
        super(controllerResources);

        addressAlert = getNotificationFactory().vBoxAlert("Modify Your Address", "");

        getNodeFactory().getAddressComboBoxes((a, b, c) -> {
            countryCombo = a;
            provinceCombo = b;
            cityCombo = c;
        }, getConfigBundle().getGeoConfig());


        addressAlert.addNodes(countryCombo, provinceCombo, cityCombo, addressLine1, addressLine2, postalCode);

    }

    /**
     * Sets event handler for this AddressAlert's confirm button.
     *
     * @param eventHandler the event handler
     */
    public void setEventHandler(EventHandler<ActionEvent> eventHandler) {
        addressAlert.setConfirmHandler(eventHandler);
    }

    /**
     * Display the AddressAlert.
     */
    public void display() {
        addressAlert.display();
    }

    /**
     * Sets the initial address for this Alert.
     *
     * @param address the address
     */
    public void setAddress(Address address) {
        if (address != null) {
            String country = address.getCountry();
            String province = address.getTerritory();
            String city = address.getCity();
            countryCombo.getSelectionModel().select(country);
            provinceCombo.setItems(getConfigBundle().getGeoConfig().getProvincesByCountry(country));
            provinceCombo.getSelectionModel().select(province);
            cityCombo.setItems(getConfigBundle().getGeoConfig().getCitiesByProvinceCountry(country, province));
            cityCombo.getSelectionModel().select(city);
            addressLine1.setText(address.getFirstAddressLine());
            addressLine2.setText(address.getSecondAddressLine());
            postalCode.setText(address.getPostalCode());
        }
    }

    /**
     * Sets country combo.
     *
     * @param countryCombo the country combo
     */
    public void setCountryCombo(ComboBox<String> countryCombo) {
        this.countryCombo = countryCombo;
    }

    /**
     * Sets province combo.
     *
     * @param provinceCombo the province combo
     */
    public void setProvinceCombo(ComboBox<String> provinceCombo) {
        this.provinceCombo = provinceCombo;
    }

    /**
     * Sets city combo.
     *
     * @param cityCombo the city combo
     */
    public void setCityCombo(ComboBox<String> cityCombo) {
        this.cityCombo = cityCombo;
    }

    /**
     * Gets country combo.
     *
     * @return the country combo
     */
    public ComboBox<String> getCountryCombo() {
        return countryCombo;
    }

    /**
     * Gets province combo.
     *
     * @return the province combo
     */
    public ComboBox<String> getProvinceCombo() {
        return provinceCombo;
    }

    /**
     * Gets city combo.
     *
     * @return the city combo
     */
    public ComboBox<String> getCityCombo() {
        return cityCombo;
    }

    /**
     * Gets address line 1.
     *
     * @return the address line 1
     */
    public TextField getAddressLine1() {
        return addressLine1;
    }

    /**
     * Gets address line 2.
     *
     * @return the address line 2
     */
    public TextField getAddressLine2() {
        return addressLine2;
    }

    /**
     * Gets postal code.
     *
     * @return the postal code
     */
    public TextField getPostalCode() {
        return postalCode;
    }

    /**
     * Sets postal code.
     *
     * @param postalCode the postal code
     */
    public void setPostalCode(TextField postalCode) {
        this.postalCode = postalCode;
    }
}

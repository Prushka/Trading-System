package phase2.trade.alert;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import phase2.trade.address.Address;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;

public class AddressAlertController extends AbstractController {

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    private TextField addressLine1 = getNodeFactory().getDefaultTextField("Address Line 1 (Optional)");
    private TextField addressLine2 = getNodeFactory().getDefaultTextField("Address Line 2 (Optional)");
    private TextField postalCode = getNodeFactory().getDefaultTextField("Postal Code (Optional)");

    private VBoxAlert addressAlert;

    public AddressAlertController(ControllerResources controllerResources) {
        super(controllerResources);

        addressAlert = getNotificationFactory().vBoxAlert("Modify Your Address", "");

        getNodeFactory().getAddressComboBoxes((a, b, c) -> {
            countryCombo = a;
            provinceCombo = b;
            cityCombo = c;
        }, getConfigBundle().getGeoConfig());


        addressAlert.addNodes(countryCombo, provinceCombo, cityCombo, addressLine1, addressLine2, postalCode);

    }

    public void setEventHandler(EventHandler<ActionEvent> eventHandler){
        addressAlert.setConfirmHandler(eventHandler);
    }

    public void display() {
        addressAlert.display();
    }

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

    public void setCountryCombo(ComboBox<String> countryCombo) {
        this.countryCombo = countryCombo;
    }

    public void setProvinceCombo(ComboBox<String> provinceCombo) {
        this.provinceCombo = provinceCombo;
    }

    public void setCityCombo(ComboBox<String> cityCombo) {
        this.cityCombo = cityCombo;
    }

    public ComboBox<String> getCountryCombo() {
        return countryCombo;
    }

    public ComboBox<String> getProvinceCombo() {
        return provinceCombo;
    }

    public ComboBox<String> getCityCombo() {
        return cityCombo;
    }

    public TextField getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(TextField addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public TextField getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(TextField addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public TextField getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(TextField postalCode) {
        this.postalCode = postalCode;
    }

    public VBoxAlert getAddressAlert() {
        return addressAlert;
    }

    public void setAddressAlert(VBoxAlert addressAlert) {
        this.addressAlert = addressAlert;
    }
}

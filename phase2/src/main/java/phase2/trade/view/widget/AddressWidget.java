package phase2.trade.view.widget;

import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import phase2.trade.address.Address;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.command.ChangeAddress;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddressWidget extends SmallTextWidgetController {

    private final Address address;

    private final Label countryLabel = new Label();
    private final Label provinceLabel = new Label();
    private final Label cityLabel = new Label();

    public AddressWidget(ControllerResources controllerResources) {
        super(controllerResources);
        address = getAccountManager().getLoggedInUser().getAddressBook().getSelectedAddress();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-i");
        String country = "";
        String province = "";
        String city = "";
        GeneralVBoxAlert addressAlert = getPopupFactory().vBoxAlert("Modify Your Address", "");
        ComboBox<String> countryCombo = getNodeFactory().getComboBox(getConfigBundle().getGeoConfig().getMap().keySet());
        ComboBox<String> provinceCombo = getNodeFactory().getComboBox();
        ComboBox<String> cityCombo = getNodeFactory().getComboBox();
        TextField addressLine1 = getNodeFactory().getDefaultTextField("Address Line 1 (Optional)");
        TextField addressLine2 = getNodeFactory().getDefaultTextField("Address Line 2 (Optional)");
        TextField postalCode = getNodeFactory().getDefaultTextField("Postal Code (Optional)");

        countryCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                provinceCombo.setItems(getConfigBundle().getGeoConfig().getProvincesByCountry(selectedCountry));
            }
        });

        provinceCombo.setOnAction(e -> {
            String selectedCountry = countryCombo.getSelectionModel().getSelectedItem();
            String selectedProvince = provinceCombo.getSelectionModel().getSelectedItem();
            if (selectedCountry != null) {
                cityCombo.setItems(getConfigBundle().getGeoConfig().getCitiesByProvinceCountry(selectedCountry, selectedProvince));
            }
        });

        addressAlert.addNodes(countryCombo, provinceCombo, cityCombo, addressLine1, addressLine2, postalCode);
        addressAlert.setEventHandler(event -> {
            ChangeAddress changeAddress = getCommandFactory().getCommand(ChangeAddress::new);
            changeAddress.execute((result, status) -> {
                        status.setSucceeded(this::refresh);
                        status.handle(getPopupFactory());
                    },
                    countryCombo.getSelectionModel().getSelectedItem(),
                    provinceCombo.getSelectionModel().getSelectedItem(), cityCombo.getSelectionModel().getSelectedItem()
                    , addressLine1.getText(), addressLine2.getText(), postalCode.getText());
        });

        if (address != null) {
            country = address.getCountry();
            province = address.getTerritory();
            city = address.getCity();
            countryCombo.getSelectionModel().select(country);
            provinceCombo.setItems(getConfigBundle().getGeoConfig().getProvincesByCountry(country));
            provinceCombo.getSelectionModel().select(province);
            cityCombo.setItems(getConfigBundle().getGeoConfig().getCitiesByProvinceCountry(country, province));
            cityCombo.getSelectionModel().select(city);
            addressLine1.setText(address.getFirstAddressLine());
            addressLine2.setText(address.getSecondAddressLine());
        }
        addTitle(countryLabel);
        addContent(provinceLabel, cityLabel);
        setOnMouseClicked(e -> addressAlert.display());
        refresh();
    }

    protected void refresh() {
        countryLabel.setText("Address - " + address.getCountry());
        provinceLabel.setText("Province - " + address.getTerritory());
        cityLabel.setText("City - " + address.getCity());
    }

}

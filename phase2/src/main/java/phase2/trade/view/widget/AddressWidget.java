package phase2.trade.view.widget;

import javafx.scene.control.Label;
import phase2.trade.address.Address;
import phase2.trade.callback.Callback;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.command.ChangeAddress;
import phase2.trade.view.window.AddressAlertController;

import java.net.URL;
import java.util.ResourceBundle;

public class AddressWidget extends SmallTextWidgetController {

    private Address address;

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
        AddressAlertController addressAlertController = getControllerFactory().getController(AddressAlertController::new);
        addressAlertController.setAddress(address);
        addTitle(countryLabel);
        addContent(provinceLabel, cityLabel);

        addressAlertController.setEventHandler(event -> {
            ChangeAddress changeAddress = getCommandFactory().getCommand(ChangeAddress::new);
            changeAddress.execute((result, status) -> {
                        status.setSucceeded(this::refresh);
                        status.handle(getPopupFactory());
                    },
                    addressAlertController.getCountryCombo().getSelectionModel().getSelectedItem(),
                    addressAlertController.getProvinceCombo().getSelectionModel().getSelectedItem(),
                    addressAlertController.getCityCombo().getSelectionModel().getSelectedItem(),
                    addressAlertController.getAddressLine1().getText(),
                    addressAlertController.getAddressLine2().getText(),
                    addressAlertController.getPostalCode().getText());
        });

        setOnMouseClicked(e -> addressAlertController.display());
        refresh();
    }

    public void refresh() {
        address = userToPresent.getAddressBook().getSelectedAddress();
        if (address != null) {
            countryLabel.setText("Country: " + address.getCountry());
            provinceLabel.setText("Province: " + address.getTerritory());
            cityLabel.setText("City: " + address.getCity());
        } else {
            countryLabel.setText("Address");
            provinceLabel.setText("Undefined"); // this technically shouldn't happen since every user is required to set the address when signing up
        }
    }

}

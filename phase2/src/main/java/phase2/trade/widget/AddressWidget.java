package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.address.Address;
import phase2.trade.alert.AddressAlert;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.command.ChangeAddress;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Address widget.
 *
 * @author Dan Lyu
 */
public class AddressWidget extends WidgetControllerBase {

    private Address address;

    private final Label countryLabel = new Label();
    private final Label provinceLabel = new Label();
    private final Label cityLabel = new Label();


    /**
     * Constructs a new Address widget.
     *
     * @param controllerResources the controller resources
     */
    public AddressWidget(ControllerResources controllerResources) {
        super(controllerResources);
        address = getAccountManager().getLoggedInUser().getAddressBook().getSelectedAddress();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-i");
        AddressAlert addressAlert = getControllerFactory().getController(AddressAlert::new);
        addressAlert.setAddress(address);
        addTitle(countryLabel);
        addContent(provinceLabel, cityLabel);

        addressAlert.setEventHandler(event -> {
            ChangeAddress changeAddress = getCommandFactory().getCommand(ChangeAddress::new);
            changeAddress.execute((result, status) -> {
                        status.setSucceeded(this::refresh);
                        status.handle(getNotificationFactory());
                    },
                    addressAlert.getCountryCombo().getSelectionModel().getSelectedItem(),
                    addressAlert.getProvinceCombo().getSelectionModel().getSelectedItem(),
                    addressAlert.getCityCombo().getSelectionModel().getSelectedItem(),
                    addressAlert.getAddressLine1().getText(),
                    addressAlert.getAddressLine2().getText(),
                    addressAlert.getPostalCode().getText());
        });

        setOnMouseClicked(e -> addressAlert.display());
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

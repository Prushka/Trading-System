package phase2.trade.widget;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import phase2.trade.address.Address;
import phase2.trade.alert.AddressAlert;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.TradeOrder;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trade address widget.
 *
 * @author Dan Lyu
 */
public class TradeAddressWidget extends TradeDetailWidget<Address> {

    private final Label countryLabel = new Label();
    private final Label provinceLabel = new Label();
    private final Label cityLabel = new Label();
    private final AddressAlert addressAlert;


    /**
     * Constructs a new Trade address widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     * @param previousAddress     the previous address
     */
    public TradeAddressWidget(ControllerResources controllerResources, TradeOrder tradeOrder, Address previousAddress) {
        super(controllerResources, tradeOrder);
        addressAlert = getControllerFactory().getController(AddressAlert::new);
        addressAlert.setAddress(previousAddress);
    }

    /**
     * Constructs a new Trade address widget.
     *
     * @param controllerResources the controller resources
     * @param tradeOrder          the trade order
     */
    public TradeAddressWidget(ControllerResources controllerResources, TradeOrder tradeOrder) {
        this(controllerResources, tradeOrder,
                controllerResources.getAccountManager().getLoggedInUser().getAddressBook().cloneSelectedAddressWithoutDetail());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-i");
        addTitle(countryLabel);
        addContent(provinceLabel, cityLabel);

        EventHandler<ActionEvent> eventEventHandler = event -> refresh();
        addressAlert.getCountryCombo().setOnAction(eventEventHandler);
        addressAlert.getCityCombo().setOnAction(eventEventHandler);
        addressAlert.getProvinceCombo().setOnAction(eventEventHandler);
        setOnMouseClicked(e -> addressAlert.display());
        refresh();
    }

    public Address getValue() {
        Address address = new Address();
        address.setCountry(addressAlert.getCountryCombo().getSelectionModel().getSelectedItem());
        address.setTerritory(addressAlert.getProvinceCombo().getSelectionModel().getSelectedItem());
        address.setCity(addressAlert.getCityCombo().getSelectionModel().getSelectedItem());
        address.setFirstAddressLine(addressAlert.getAddressLine1().getText());
        address.setSecondAddressLine(addressAlert.getAddressLine2().getText());
        address.setPostalCode(addressAlert.getPostalCode().getText());
        return address;
    }

    public void refresh() {
        countryLabel.setText("Country: " + addressAlert.getCountryCombo().getSelectionModel().getSelectedItem());
        provinceLabel.setText("Province: " + addressAlert.getProvinceCombo().getSelectionModel().getSelectedItem());
        cityLabel.setText("City: " + addressAlert.getCityCombo().getSelectionModel().getSelectedItem());

    }
}

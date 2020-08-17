package phase2.trade.view.widget;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import phase2.trade.address.Address;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;
import phase2.trade.user.command.ChangeAddress;
import phase2.trade.view.window.AddressAlertController;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeAddressWidget extends TradeDetailWidget {

    private final Label countryLabel = new Label();
    private final Label provinceLabel = new Label();
    private final Label cityLabel = new Label();
    private final AddressAlertController addressAlertController;


    public TradeAddressWidget(ControllerResources controllerResources, User leftSelected, User rightSelected) {
        super(controllerResources, leftSelected, rightSelected);
        Address address = getAccountManager().getLoggedInUser().getAddressBook().cloneSelectedAddressWithoutDetail();
        addressAlertController = getControllerFactory().getController(AddressAlertController::new);
        addressAlertController.setAddress(address);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-i");
        addTitle(countryLabel);
        addContent(provinceLabel, cityLabel);

        EventHandler<ActionEvent> eventEventHandler = event -> refresh();
        addressAlertController.getCountryCombo().setOnAction(eventEventHandler);
        addressAlertController.getCityCombo().setOnAction(eventEventHandler);
        addressAlertController.getProvinceCombo().setOnAction(eventEventHandler);
        setOnMouseClicked(e -> addressAlertController.display());
        refresh();
    }

    public Address getSubmittedAddress() {
        Address address = new Address();
        address.setCountry(addressAlertController.getCountryCombo().getSelectionModel().getSelectedItem());
        address.setTerritory(addressAlertController.getProvinceCombo().getSelectionModel().getSelectedItem());
        address.setCity(addressAlertController.getCityCombo().getSelectionModel().getSelectedItem());
        address.setFirstAddressLine(addressAlertController.getAddressLine1().getText());
        address.setSecondAddressLine(addressAlertController.getAddressLine2().getText());
        address.setPostalCode(addressAlertController.getPostalCode().getText());
        return address;
    }

    public void refresh() {
        countryLabel.setText("Country: " + addressAlertController.getCountryCombo().getSelectionModel().getSelectedItem());
        provinceLabel.setText("Province: " + addressAlertController.getProvinceCombo().getSelectionModel().getSelectedItem());
        cityLabel.setText("City: " + addressAlertController.getCityCombo().getSelectionModel().getSelectedItem());

    }
}

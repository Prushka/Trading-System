package phase2.trade.trade.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import phase2.trade.alert.VBoxAlert;
import phase2.trade.controller.AbstractListController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.Trade;
import phase2.trade.trade.command.GetTrades;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_list_view.fxml")
public class TradeListController extends AbstractListController<Trade> implements Initializable {

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    GetTrades getTrades = getCommandFactory().getCommand(GetTrades::new);

    public TradeListController(ControllerResources controllerResources) {
        super(controllerResources, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getTrades.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(result);
                afterFetch();
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    @Override
    public void reload() {
        getTrades.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                reloadNewDisplayData(result);
            });
            resultStatus.handle(getNotificationFactory());
        });
        super.reload();
    }

    private void afterFetch() {
        listView.setCellFactory(param -> new TradeCell());

        listView.setOnMouseClicked(event -> {
            displayPopup(listView.getSelectionModel().getSelectedItem());
        });
        listViewGenerator.build();
    }

    private void displayPopup(Trade trade) {
        VBoxAlert VBoxAlert = getNotificationFactory().vBoxAlert("", "", "Ok");
        TradeEditController tradeEditController = new TradeEditController(getControllerResources(), trade);
        VBoxAlert.addNodes(getSceneManager().loadPane(tradeEditController));
        VBoxAlert.display();
    }
}

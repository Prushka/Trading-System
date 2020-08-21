package phase2.trade.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.alert.VBoxAlert;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.ListController;
import phase2.trade.trade.Trade;
import phase2.trade.trade.command.GetTrades;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Trade list controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_list_view.fxml")
public class TradeListController extends ListController<Trade> implements Initializable {

    /**
     * The Get trades.
     */
    GetTrades getTrades = getCommandFactory().getCommand(GetTrades::new);

    /**
     * Constructs a new Trade list controller.
     *
     * @param controllerResources the controller resources
     */
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
        VBoxAlert alert = getNotificationFactory().vBoxAlert("", "", "Ok");
        TradeEditController tradeEditController = new TradeEditController(getControllerResources(), trade, alert);
        alert.addNodes(getSceneManager().loadPane(tradeEditController));
        alert.display();
    }

    @Override
    public void refresh() {
        listView.refresh();
        super.refresh();
    }
}

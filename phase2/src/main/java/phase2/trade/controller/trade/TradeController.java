package phase2.trade.controller.trade;

import javafx.fxml.Initializable;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeManager;

import java.net.URL;
import java.util.ResourceBundle;


public class TradeController extends AbstractController implements Initializable {

    private final TradeManager tm;

    Trade newTrade;

    public TradeController(ControllerResources controllerResources) {
        super(controllerResources);
        this.tm = new TradeManager(getGatewayBundle().getConfigBundle().getTradeConfig());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Add getRecentTrades, getFrequentTraders, etc.
}

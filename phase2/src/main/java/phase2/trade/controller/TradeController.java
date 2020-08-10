package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.callback.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.item.Item;
import phase2.trade.presenter.SceneManager;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeManager;
import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;


public class TradeController extends AbstractController implements Initializable {

    private final TradeGateway tradeGateway;

    private final TradeManager tm;

    Trade newTrade;

    public TradeController(SceneManager sceneManager) {
        super(sceneManager);
        this.tradeGateway = getGatewayBundle().getEntityBundle().getTradeGateway();
        this.tm = new TradeManager(getGatewayBundle().getConfigBundle().getTradeConfig());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Add getRecentTrades, getFrequentTraders, etc.
}

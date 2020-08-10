package phase2.trade.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import phase2.trade.trade.Trade;

import java.net.URL;
import java.util.ResourceBundle;

public class TradeInfoPresenter implements Initializable {

    // have not done xml yet
    public Label userId = new Label();
    public Label tradeState = new Label();
    public Label isPermanent = new Label();


    private final Trade trade;

    public TradeInfoPresenter(Trade trade) {
        this.trade = trade;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userId.setText("User Id: " + trade.getUid());
        tradeState.setText("Trade State: " + trade.getTradeState());
        isPermanent.setText("Email: " + trade.getIsPermanent());

    }
}

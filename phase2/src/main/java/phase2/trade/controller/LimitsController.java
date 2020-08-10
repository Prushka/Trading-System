package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.SceneManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LimitsController extends AbstractController implements Initializable {

    public LimitsController(SceneManager sceneManager){
        super(sceneManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        JFXButton borrowFirstButton = new JFXButton("Change the Limit ");
        JFXButton transactionLimitButton = new JFXButton("Delete");
        JFXButton editLimitButton = new JFXButton("I wanna sell them");
        JFXButton openTradeLimitButton = new JFXButton("I wanna lend them");
        JFXButton timeForTradebackLimitButton = new JFXButton("I wanna lend them");
    }
}

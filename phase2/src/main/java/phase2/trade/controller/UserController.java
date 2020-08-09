package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.gateway.database.UserDAO;

import java.net.URL;
import java.util.ResourceBundle;

public class UserController extends AbstractController implements Initializable {

    private final UserDAO userDAO;

    private final GatewayBundle gatewayBundle;


    public UserController(GatewayBundle gatewayBundle, UserDAO userDAO, GatewayBundle gatewayBundle1) {
        super(gatewayBundle);
        this.userDAO = userDAO;
        this.gatewayBundle = gatewayBundle1;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}

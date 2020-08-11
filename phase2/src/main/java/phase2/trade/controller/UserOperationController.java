package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.command.GetCommands;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.presenter.SceneManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserOperationController extends AbstractController implements Initializable {


    public UserOperationController(SceneManager sceneManager) {
        super(sceneManager);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Command<List<Command>> getCommands = new GetCommands(getGatewayBundle());

        getCommands.execute(new StatusCallback<List<Command>>() {
            @Override
            public void call(List<Command> result, ResultStatus resultStatus) {
                afterFetch(result);
            }
        });

    }

    public void afterFetch(List<Command> result) {

    }

}
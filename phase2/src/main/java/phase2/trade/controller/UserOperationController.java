package phase2.trade.controller;

import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.command.GetCommands;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "a.fxml")
public class UserOperationController extends AbstractController implements Initializable {


    public UserOperationController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Command<List<Command>> getCommands = getCommandFactory().getCommand(GetCommands::new);

        getCommands.execute((result, resultStatus) -> afterFetch(result));

    }

    public void afterFetch(List<Command> result) {

    }

}

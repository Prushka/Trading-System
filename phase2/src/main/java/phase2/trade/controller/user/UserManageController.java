package phase2.trade.controller.user;

import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.GeneralTableViewController;
import phase2.trade.user.User;
import phase2.trade.user.command.GetUsers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class UserManageController extends GeneralTableViewController<User> implements Initializable {


    public UserManageController(ControllerResources controllerResources) {
        super(controllerResources, false, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<User>> getUsers = getCommandFactory().getCommand(GetUsers::new);

        getUsers.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> afterFetch(result));
            resultStatus.handle(getPopupFactory());
        });

    }

    public void afterFetch(List<User> result) {
        System.out.println(result.size());
        setDisplayData(FXCollections.observableArrayList(result));
        tableViewGenerator.addColumn("UserName", "name").addColumn("Email", "email")
                .addColumn("Permission Group", "permissionGroup").addColumn("Permissions", "permissionSet")
                .addColumn("reputation", "reputation").addColumn("point", "point");

        addSearchField("Search User Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        addSearchField("Search Email", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getEmail()).toLowerCase().contains(lowerCaseFilter);
        });

        tableViewGenerator.build();
    }
}

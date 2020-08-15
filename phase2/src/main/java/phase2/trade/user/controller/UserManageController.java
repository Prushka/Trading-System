package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.AbstractTableController;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;
import phase2.trade.user.command.CreateUser;
import phase2.trade.user.command.GetUsers;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class UserManageController extends AbstractTableController<User> implements Initializable {


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

    public void afterFetch(List<User> users) {
        setDisplayData(FXCollections.observableArrayList(users));
        tableViewGenerator.addColumn("UserName", "name").addColumn("Email", "email")
                .addColumn("Permission Group", "permissionGroup")
                .addColumn("Permissions", "permissionSet", getConfigBundle().getUiConfig().getPermissionPrefWidth())
                .addColumn("reputation", "reputation").addColumn("point", "point");

        addSearchField("Search User Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        addSearchField("Search Email", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getEmail()).toLowerCase().contains(lowerCaseFilter);
        });

        addComboBox(FXCollections.observableArrayList(Arrays.asList(Stream.of(PermissionGroup.values()).map(PermissionGroup::name).toArray(String[]::new)))
                , "Permission Group", "ALL", (entity, toMatch) -> entity.getPermissionGroup().name().equalsIgnoreCase(toMatch));

        JFXButton createUser = new JFXButton("Create a New User");

        hBox.getChildren().add(createUser);

        tableViewGenerator.build();


        TextField userName = getNodeFactory().getDefaultTextField("User Name");
        TextField email = getNodeFactory().getDefaultTextField("Email");
        TextField password = getNodeFactory().getDefaultTextField("Password");

        ComboBox<String> permissionGroup = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(PermissionGroup.values()).map(PermissionGroup::name).toArray(String[]::new))));

        GeneralVBoxAlert createUserAlert = getPopupFactory().vBoxAlert("Create New User", "");
        createUserAlert.addNodes(userName, email, password, permissionGroup);
        createUserAlert.setEventHandler(event -> {
            CreateUser command = getCommandFactory().getCommand(CreateUser::new);
            command.execute(((result, status) -> {
                        status.setExist(() -> getPopupFactory().toast(5, "Such User Name Already Exists"));
                        status.setSucceeded(() -> getPane("centerDashboard").getChildren().setAll(getSceneManager().loadPane(UserManageController::new)));
                        status.handle(getPopupFactory());
                    }
                    ),
                    userName.getText(), email.getText(), password.getText(), "country", "city", permissionGroup.getSelectionModel().getSelectedItem());
        });

        createUser.setOnAction(a -> createUserAlert.display());

    }
}

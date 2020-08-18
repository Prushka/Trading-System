package phase2.trade.user.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import phase2.trade.alert.GeneralVBoxAlert;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.User;
import phase2.trade.user.command.CreateUser;
import phase2.trade.user.command.GetUsers;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class UserManageController extends UserTableController implements Initializable {

    public UserManageController(ControllerResources controllerResources) {
        super(controllerResources, true, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<User>> getUsers = getCommandFactory().getCommand(GetUsers::new);

        getUsers.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> afterFetch(result));
            resultStatus.handle(getNotificationFactory());
        });

    }

    @Override
    public void reload() {
        Command<List<User>> getUsers = getCommandFactory().getCommand(GetUsers::new);

        getUsers.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                reloadNewDisplayData(result);
                super.reload();
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    public void afterFetch(List<User> users) {
        setDisplayData(FXCollections.observableArrayList(users));

        addNameColumn(true);
        addEmailColumn(true);
        addPermissionGroupColumn(true);
        addPermissionsColumn();

        addSearchName();
        addSearchEmail();
        addPermissionGroupComboBox();

        JFXButton createUser = new JFXButton("Create a New User");

        buttonPane.getChildren().add(createUser);

        tableViewGenerator.build();


        TextField userName = getNodeFactory().getDefaultTextField("User Name");
        TextField email = getNodeFactory().getDefaultTextField("Email");
        TextField password = getNodeFactory().getDefaultTextField("Password");

        ComboBox<String> permissionGroup = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(PermissionGroup.values()).map(PermissionGroup::name).toArray(String[]::new))));

        GeneralVBoxAlert createUserAlert = getNotificationFactory().vBoxAlert("Create New User", "");
        createUserAlert.addNodes(userName, email, password, permissionGroup);
        createUserAlert.setEventHandler(event -> {
            CreateUser command = getCommandFactory().getCommand(CreateUser::new);
            command.execute(((result, status) -> {
                        status.setExist(() -> getNotificationFactory().toast(5, "Such User Name Already Exists"));
                        status.setSucceeded(() -> getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(UserManageController::new)));
                        status.handle(getNotificationFactory());
                    }
                    ),
                    userName.getText(), email.getText(), password.getText(), permissionGroup.getSelectionModel().getSelectedItem(), "Canada", "Ontario", "Toronto");
        });

        createUser.setOnAction(a -> createUserAlert.display());

    }
}

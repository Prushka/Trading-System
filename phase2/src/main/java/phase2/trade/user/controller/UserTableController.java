package phase2.trade.user.controller;

import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractEditableTableController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.UserEditor;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.refresh.ReType;
import phase2.trade.user.User;
import phase2.trade.user.command.UpdateUsers;

import java.util.List;

public class UserTableController extends AbstractEditableTableController<User, UserEditor> implements Initializable {

    public UserTableController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable, UserEditor::new);
    }

    @Override
    public void refresh() {
        tableView.refresh();
    }

    @Override
    protected void updateEntity(List<User> entities) {
        disableButtons(true);
        Command<?> command = getCommandFactory().getCommand(UpdateUsers::new, c -> {
            c.setToUpdate(entities);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                publishGateway();
                publish(ReType.REFRESH);
            });
            resultStatus.setAfter(() -> {
                disableButtons(false);
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    @Override
    public void reload() {
        tableView.refresh();
        super.reload();
    }

    protected void addNameColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Name", "name", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, UserEditor::alterName));
        } else {
            tableViewGenerator.addColumn("Name", "name");
        }
    }

    protected void addEmailColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Email", "email", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, UserEditor::alterEmail));
        } else {
            tableViewGenerator.addColumn("Email", "Email");
        }
    }

    protected void addPermissionGroupColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Permission Group", "permissionGroup",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, UserEditor::alterPermissionGroup),
                    getNodeFactory().getEnumAsObservableString(PermissionGroup.class));
        } else {
            tableViewGenerator.addColumn("Permission Group", "permissionGroup");
        }
    }

    protected void addPermissionsColumn() {
        tableViewGenerator.addColumn("Permissions", "permissionSet", getConfigBundle().getUiConfig().getPermissionPrefWidth());
    }


    protected void addSearchName() {
        addSearchField("Search User Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    protected void addSearchEmail() {
        addSearchField("Search Email", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getEmail()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    protected void addPermissionGroupComboBox() {
        addComboBox(getNodeFactory().getEnumAsObservableString(PermissionGroup.class)
                , "Permission Group", "ALL", (entity, toMatch) -> entity.getPermissionGroup().name().equalsIgnoreCase(toMatch));
    }


}

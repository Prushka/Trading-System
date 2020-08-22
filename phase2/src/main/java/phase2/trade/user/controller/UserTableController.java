package phase2.trade.user.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableTableController;
import phase2.trade.editor.UserEditor;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.refresh.ReType;
import phase2.trade.user.User;
import phase2.trade.user.command.UpdateUsers;

import java.util.List;

/**
 * The User table controller.
 *
 * @author Dan Lyu
 */
public class UserTableController extends EditableTableController<User, UserEditor> implements Initializable {

    /**
     * Constructs a new User table controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     * @param ifEditable          the if editable
     */
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

    /**
     * Add name column.
     *
     * @param editable the editable
     */
    protected void addNameColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Name", "name", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, UserEditor::alterName));
        } else {
            tableViewGenerator.addColumn("Name", "name");
        }
    }

    /**
     * Add email column.
     *
     * @param editable the editable
     */
    protected void addEmailColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Email", "email", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, UserEditor::alterEmail));
        } else {
            tableViewGenerator.addColumn("Email", "Email");
        }
    }

    protected void addAddressColumn() {
        tableViewGenerator.addColumn("Address", param -> {
            if (param.getValue().getAddressBook() == null || param.getValue().getAddressBook().getSelectedAddress() == null) {
                return new SimpleStringProperty("null");
            }
            return new SimpleStringProperty(String.format("%s,%s,%s", param.getValue().getAddressBook().getSelectedAddress().getCountry(), param.getValue().getAddressBook().getSelectedAddress().getTerritory(),
                    param.getValue().getAddressBook().getSelectedAddress().getCity()));
        });
    }

    /**
     * Add permission group column.
     *
     * @param editable the editable
     */
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

    /**
     * Add permissions column.
     */
    protected void addPermissionsColumn() {
        tableViewGenerator.addColumn("Permissions", "permissionSet", getConfigBundle().getUiConfig().getPermissionPrefWidth());
    }


    /**
     * Add search name.
     */
    protected void addSearchName() {
        addSearchField("Search User Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    /**
     * Add search email.
     */
    protected void addSearchEmail() {
        addSearchField("Search Email", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getEmail()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    /**
     * Add permission group combo box.
     */
    protected void addPermissionGroupComboBox() {
        addComboBox(getNodeFactory().getEnumAsObservableString(PermissionGroup.class)
                , "Permission Group", "ALL", (entity, toMatch) -> entity.getPermissionGroup().name().equalsIgnoreCase(toMatch));
    }


}

package phase2.trade.user.controller;

import javafx.fxml.Initializable;
import phase2.trade.controller.AbstractEditableTableController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.UserEditor;
import phase2.trade.user.User;

import java.util.List;

public class UserTableController extends AbstractEditableTableController<User, UserEditor> implements Initializable {

    public UserTableController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable, UserEditor::new, User::getUid);
    }

    @Override
    protected void updateEntity(List<User> entities) {

    }
}

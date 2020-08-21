package phase2.trade.trade.controller;

import com.jfoenix.controls.JFXListCell;
import phase2.trade.user.User;

/**
 * The User cell used to present User in a ComboBox.
 *
 * @author Dan Lyu
 */
public class UserCell extends JFXListCell<User> {

    private final Long loggedInUserId;

    /**
     * Constructs a new User cell.
     *
     * @param loggedInUserId the logged in user id
     */
    public UserCell(Long loggedInUserId) {
        this.loggedInUserId = loggedInUserId;
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty) {
            setGraphic(null);
        } else {
            if (loggedInUserId.equals(user.getUid())) {
                setText(user.getName() + " (You)");
                return;
            }
            setText(user.getName());
        }
    }
}
package phase2.trade.item.controller;

import com.jfoenix.controls.JFXListCell;
import phase2.trade.user.User;

public class UserCell extends JFXListCell<User> {

    private final Long loggedInUserId;

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
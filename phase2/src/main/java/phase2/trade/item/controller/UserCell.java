package phase2.trade.item.controller;

import com.jfoenix.controls.JFXListCell;
import phase2.trade.user.User;

public class UserCell extends JFXListCell<User> {

    @Override
    protected void updateItem(User item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setText(item.getName());
        }
    }
}
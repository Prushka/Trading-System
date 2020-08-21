package phase2.trade.item.controller;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import phase2.trade.user.User;

/**
 * The User string converter.
 *
 * @author Dan Lyu
 */
public class UserStringConverter extends StringConverter<User> {

    private final Long loggedInUserId;

    private final ComboBox<User> comboBox;

    /**
     * Constructs a new User string converter.
     *
     * @param comboBox       the combo box
     * @param loggedInUserId the logged in user id
     */
    public UserStringConverter(ComboBox<User> comboBox, Long loggedInUserId) {
        this.comboBox = comboBox;
        this.loggedInUserId = loggedInUserId;
    }

    @Override
    public String toString(User object) {
        if (object.getUid().equals(loggedInUserId)) {
            return object.getName() + " (You)";
        }
        return object.getName();
    }

    @Override
    public User fromString(String string) {
        return comboBox.getItems().stream().filter(ap ->
                ap.getName().equals(string)).findFirst().orElse(null);
    }

}

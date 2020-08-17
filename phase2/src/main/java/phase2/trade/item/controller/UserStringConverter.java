package phase2.trade.item.controller;

import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;
import phase2.trade.user.User;

public class UserStringConverter extends StringConverter<User> {

    private ComboBox<User> comboBox;

    public UserStringConverter(ComboBox<User> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public String toString(User object) {
        return object.getName();
    }

    @Override
    public User fromString(String string) {
        return comboBox.getItems().stream().filter(ap ->
                ap.getName().equals(string)).findFirst().orElse(null);
    }

}

package phase2.trade.user;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.controller.Editor;

import java.util.ArrayList;
import java.util.List;

public class UserEditor extends Editor<User> {

    public UserEditor(List<User> entities) {
        super(entities);
    }

    public UserEditor(User entity) {
        super(entity);
    }
}

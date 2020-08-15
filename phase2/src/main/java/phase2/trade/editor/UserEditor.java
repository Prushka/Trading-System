package phase2.trade.editor;

import phase2.trade.user.User;

import java.util.List;

public class UserEditor extends Editor<User> {

    public UserEditor(List<User> entities) {
        super(entities);
    }

    public UserEditor(User entity) {
        super(entity);
    }
}

package phase2.trade.user;

import java.util.HashSet;

public class UserPermissionFactory {

    public enum Group {
        ADMIN, GUEST, OWNER
    }

    // make this configurable

    public UserPermission getUserPermission(Group type) {
        switch (type) {
            case ADMIN:
                return new UserPermission(new HashSet<Permission>() {{
                    add(Permission.REMOVE_ITEM);
                    add(Permission.REVIEW_ITEM);
                }});
        }
        return null;
    }
}

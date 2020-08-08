package phase2.trade.permission;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;

import java.util.HashSet;

public class PermissionGroupFactory {

    // make this configurable

    public PermissionSet getUserPermission(PermissionGroup type) {
        switch (type) {
            case ADMIN:
                return new PermissionSet(new HashSet<Permission>() {{
                    add(Permission.REMOVE_ITEM);
                    add(Permission.REVIEW_ITEM);
                }});
        }
        return null;
    }
}

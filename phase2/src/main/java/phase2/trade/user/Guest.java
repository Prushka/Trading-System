package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

public class Guest extends User {

    public Guest() {
        setPermissionGroup(PermissionGroup.GUEST);
    }

    public PermissionGroup getPermissionGroup() {
        return PermissionGroup.GUEST;
    }
}

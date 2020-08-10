package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

public class System extends User {

    public System() {
        setPermissionGroup(PermissionGroup.SYSTEM);
        setUserName("system");
    }

}

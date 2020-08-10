package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;

@Entity
public class System extends User {


    public System() {
        setPermissionGroup(PermissionGroup.SYSTEM);
        setUserName("system");
    }

}

package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;

@Entity
public class SystemUser extends User {


    public SystemUser() {
        setPermissionGroup(PermissionGroup.SYSTEM);
        setUserName("system");
    }

}

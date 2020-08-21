package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;

/**
 * The System user.
 *
 * @author Dan Lyu
 */
@Entity
public class SystemUser extends User {


    /**
     * Constructs a new System user.
     */
    public SystemUser() {
        setPermissionGroup(PermissionGroup.SYSTEM);
        setName("system");
    }

}

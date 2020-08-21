package phase2.trade.user;


import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;

/**
 * The Administrative user.
 *
 * @author Dan Lyu
 */
@Entity
public class AdministrativeUser extends User {


    /**
     * Constructs a new Administrative user.
     *
     * @param userName the user name
     * @param email    the email
     * @param password the password
     */
    public AdministrativeUser(String userName, String email, String password) {
        super(userName, email, password);
        setPermissionGroup(PermissionGroup.ADMIN);
    }

    /**
     * Constructs a new Administrative user.
     */
    public AdministrativeUser() {
    }
}

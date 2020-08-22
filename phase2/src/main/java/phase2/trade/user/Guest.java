package phase2.trade.user;

import phase2.trade.permission.PermissionGroup;

/**
 * The Guest.
 *
 * @author Dan Lyu
 */
public class Guest extends User {

    /**
     * Constructs a new Guest.
     *
     * @param userName the user name
     * @param email    the email
     * @param password the password
     */
    public Guest(String userName, String email, String password) {
        super(userName, email, password);
        setUid(-1L);
    }

    /**
     * Constructs a new Guest.
     */
    public Guest() {
        setPermissionGroup(PermissionGroup.GUEST);
        setUid(-1L);
    }

    public PermissionGroup getPermissionGroup() {
        return PermissionGroup.GUEST;
    }
}

package phase2.trade.user;

import phase2.trade.address.AddressBook;
import phase2.trade.permission.PermissionGroup;

public class Guest extends User {

    public Guest(String userName, String email, String password) {
        super(userName, email, password);
    }

    public Guest() {
        setPermissionGroup(PermissionGroup.GUEST);
    }

    public PermissionGroup getPermissionGroup() {
        return PermissionGroup.GUEST;
    }
}

package phase2.trade.user;

import phase2.trade.address.AddressBook;
import phase2.trade.permission.PermissionGroup;

public class Guest extends User {

    public Guest(String userName, String email, String password, String country, String city) {
        super(userName, email, password, country, city);
    }

    public Guest() {
        setPermissionGroup(PermissionGroup.GUEST);
    }

    public PermissionGroup getPermissionGroup() {
        return PermissionGroup.GUEST;
    }
}

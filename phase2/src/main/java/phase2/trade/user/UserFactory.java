package phase2.trade.user;

import phase2.trade.config.PermissionConfig;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

public class UserFactory {

    private PermissionConfig permissionConfig;

    public UserFactory(PermissionConfig permissionConfig) {
        this.permissionConfig = permissionConfig;
    }

    public User createByPermissionGroup(String userName, String email, String password, String country, String city, String permissionGroupString) { // country and city are not necessary for all users
        PermissionGroup permissionGroup = PermissionGroup.valueOf(permissionGroupString);
        User user;
        switch (permissionGroup) {
            case ADMIN:
            case HEAD_ADMIN:
                user = new AdministrativeUser(userName, email, password, country, city);
                break;

            case REGULAR:
                user = new RegularUser(userName, email, password, country, city);
                break;

            case SYSTEM:
                user = new SystemUser();
                break;

            default:
                user = new Guest(userName, email, password, country, city);
                break;
        }
        user.setPermissionGroup(permissionGroup);
        PermissionSet fromConfig = permissionConfig.getDefaultPermissions().get(permissionGroup);
        if (fromConfig != null) {
            user.setUserPermission(new PermissionSet(fromConfig.getPerm()));
        } else{
            System.err.println("Permission Not Set For Group: "+permissionGroup);
        }
        return user;
    }

    public User configureSystemUser() { // system user will not be persistent
        return createByPermissionGroup("SYSTEM", "SYSTEM", "", "MARS", "", PermissionGroup.SYSTEM.name());
    }

    public User configureGuest() { // system user will not be persistent
        return createByPermissionGroup("Guest", "guest@example.com", "", "MARS", "", PermissionGroup.GUEST.name());
    }
}

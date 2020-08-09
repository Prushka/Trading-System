package phase2.trade.user;


import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {

    private PermissionGroup permissionGroup;

    public AdministrativeUser(String userName, String email, String password, String country, String city) {
        super(userName, email, password, country, city);
        this.permissionGroup = PermissionGroup.ADMIN;
    }

    public AdministrativeUser(String userName, String email, String password, String country, String city, boolean isHead) {
        super(userName, email, password, country, city);
        this.permissionGroup = PermissionGroup.HEADADMIN;
    }

    public AdministrativeUser(){}


    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

}

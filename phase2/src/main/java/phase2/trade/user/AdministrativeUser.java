package phase2.trade.user;


import phase2.trade.permission.PermissionGroup;

import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {


    public AdministrativeUser(String userName, String email, String password) {
        super(userName, email, password);
        setPermissionGroup(PermissionGroup.ADMIN);
    }

    public AdministrativeUser() {
    }
}

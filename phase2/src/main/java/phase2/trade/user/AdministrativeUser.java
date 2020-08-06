package phase2.trade.user;


import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {

    public AdministrativeUser(String userName, String email, String password) {
        super(userName, email, password);
    }

    public AdministrativeUser() {

    }

}

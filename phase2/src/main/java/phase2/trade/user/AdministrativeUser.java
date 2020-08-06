package phase2.trade.user;


import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {

    public AdministrativeUser(String userName, String email, String password, String country, String city) {
        super(userName, email, password, country, city);
    }

    public AdministrativeUser() {}

}

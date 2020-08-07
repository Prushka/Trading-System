package phase2.trade.user;


import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {

    private AccountState accountState;

    public AdministrativeUser(String userName, String email, String password, String country, String city) {
        super(userName, email, password, country, city);
        //accountState =;
    }

    public AdministrativeUser() {}

    public AccountState getAccountState() {
        return accountState;
    }

}

package phase2.trade.user;


import javax.persistence.Entity;
import java.util.List;

@Entity
public class AdministrativeUser extends User {

    private boolean isHead;

    public AdministrativeUser(String userName, String email, String password, boolean isHead) {
        super(userName, email, password);
        this.isHead = isHead;
    }

    public AdministrativeUser() {

    }

    public boolean getIsHead() {
        return this.isHead;
    }

    public void setIsHead(boolean isHead) {
        this.isHead = isHead;
    }

}

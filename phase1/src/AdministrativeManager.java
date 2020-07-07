import java.util.ArrayList;
import java.util.List;

public class AdministrativeManager {

    private static List<AdministrativeUser> administrators = new ArrayList<>();
    //private List<Notification> notifications ;
    private static int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;

    public AdministrativeManager(AdministrativeUser administrativeUser){
        //what does the constractor do???
        if (this.administrators.contains(administrativeUser) == false) {
            if (administrativeUser.getIsHead() == true) {
                this.administrators.add(administrativeUser);
            }
        }//else //throw expectaion?

    }


    public boolean verifyLogin(String username, String password){
        for (AdministrativeUser admin: administrators){
            if (admin.getUserName().equals(username) && admin.getPassword().equals(password)){
                return true;
            }
        }
        return false;  //maybe throw expectation?? or return string to say wrong username or wrong password
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String password, boolean isHead){
        if (head.getIsHead()){
            AdministrativeUser sub = new AdministrativeUser(username, email, password, false);
            administrators.add(sub);
            return true;
        } else{
            return false;
        }
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password, boolean isHead){
        if (head.getIsHead()){
            AdministrativeUser sub = new AdministrativeUser(username, email, telephone, password, false);
            administrators.add(sub);
            return true;
        } else{
            return false;
        }
    }

    public int getTransactionLimit(){
        return this.transactionLimit;
    }

    public void setTransactionLimit(int limit){
        transactionLimit = limit;
    }

    public int getLendBeforeBorrow(){
        return this.lendBeforeBorrow;
    }

    public void setLendBeforeBorrow(int limit){
        lendBeforeBorrow = limit;
    }

    public void freezeUser(PersonalUser user){
        user.setIsFrozen(true);
    }

    public void unfreezeUser(PersonalUser user){
        user.setIsFrozen(false);
    }

    public boolean removeItem(PersonalUser user, Object item){      //TODO:replace object with item
        return (user.getInventory()).remove(item);
    }

    public boolean addItem(PersonalUser user, Object item){      //TODO:replace object with item
        (user.getInventory()).add(item);
        return true;
    }
}

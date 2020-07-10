package group;

import java.util.ArrayList;
import java.util.List;

public class AdministrativeManager {

    private static final List<AdministrativeUser> administrators = new ArrayList<>();
    private static List<PersonalUser> frozennotifications ;
    private static int transactionLimit = 100; //what is the init limit?
    private static int lendBeforeBorrow = 1;

    public AdministrativeManager(){
    }

    public void createadministrator(String username, String email, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, password, isHead);
        administrators.add(admin);
    }

    public void createadministrator(String username, String email, String telephone, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, isHead);
        administrators.add(admin);
    }

    public boolean verifyLogin(String username, String password){
        for (AdministrativeUser admin: administrators){
            if (admin.getUserName().equals(username) && admin.getPassword().equals(password)){
                return true;
            }
        }
        return false;  //maybe throw expectation?? or return string to say wrong username or wrong password
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String password){
        if (head.getIsHead()){
            createadministrator(username, email, password, false);
            return true;
        } else{
            return false;
        }
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createadministrator(username, email, telephone, password, false);
            return true;
        } else{
            return false;
        }
    }

    public int getTransactionLimit(){
        return transactionLimit;
    }

    public void setTransactionLimit(int limit){
        transactionLimit = limit;
    }

    public int getLendBeforeBorrow(){
        return lendBeforeBorrow;
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

    public boolean removeItem(PersonalUser user, Item item){
        return (user.getInventory()).remove(item);
    }

    public boolean addItem(PersonalUser user, Item item){
        (user.getInventory()).add(item);
        return true;
    }

    public void createFrozenNotification(PersonalUser user){
        frozennotifications.add(user);
    }

    public void freezeListUser(){
        for (PersonalUser p : frozennotifications){
            freezeUser(p);
            frozennotifications.remove(p);
        }
    }

    //TODO: method of creating notification and add them to notification list or repo
}

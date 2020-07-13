package group.user;

import group.item.Item;
import group.repository.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AdministrativeManager { //TODO where to find request of unfreeze user and request of adding book


    private Repository<AdministrativeUser> administrators;
    private Repository<PersonalUser> personalUserRepository;
    private Iterator<PersonalUser> needToFreezelist;
    private int transactionLimit = 100; //what is the init limit?
    private int lendBeforeBorrow = 1;

    public AdministrativeManager(Repository<AdministrativeUser> administrativeUserRepository,
                                 Repository<PersonalUser> personalUserRepository){
        this.administrators = administrativeUserRepository;
        this.personalUserRepository = personalUserRepository;
        needToFreezelist = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
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
         return administrators.ifExists(
                 AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                         && AdministrativeUser.getPassword().equals(password));
    }  //if FALSE maybe throw expectation?? or return string to say wrong username or wrong password


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

    public int getLendBeforeBorrowLimit(){
        return lendBeforeBorrow;
    }

    public void setLendBeforeBorrowLimit(int limit){
        lendBeforeBorrow = limit;
    }

    public Iterator<PersonalUser>getListUserShouldBeFreezed(){
        return needToFreezelist;
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

    public boolean confirmAddItem(PersonalUser user, Item item){ //TODO where do admin get the request of adding item
        (user.getInventory()).add(item);
        return true;
    }

    public void confirmFreezeUser(PersonalUser user) {
        freezeUser(user);
    }

    public void confirmFreezeAllUser(){
        while (needToFreezelist.hasNext()){
            freezeUser(needToFreezelist.next());
        }

    }

    //public void exampleOfFilter() {
        //Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezed);
        //Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount()); // they are the same
        // this iterator has all PersonalUsers that need to be frozen
    //}


        //public void findFreezeUser() {
        //r}


}

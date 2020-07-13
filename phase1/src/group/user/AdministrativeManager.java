package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Repository;
import group.trade.Trade;

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

    private Response adminUserRepresentation(String translatable, User user) {
        return new Response.Builder(true).
                translatable(translatable, user.getName(), user.getEmail(), user.getPassword())
                .build();
    }

    public Response createAdministrator(String username, String email, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, password, isHead);
        administrators.add(admin);
        return adminUserRepresentation("filler", this.findAdmin(username)); // filler needs to be established in language.properties? I'm not sure if this is to be hardcoded or
                                                                                        // if generateLanguage does this somehow?
    }

    public void createAdministrator(String username, String email, String telephone, String password, boolean isHead){
        AdministrativeUser admin = new AdministrativeUser(username, email, telephone, password, isHead);
        administrators.add(admin);
        // return adminUserRepresentation("filler", this.findAdmin(username)); // not sure why we have two constructors
    }

    public Response verifyLogin(String username, String password) { // changed return type to response, this allows interaction with controller
        if(administrators.ifExists(
                 AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                         && AdministrativeUser.getPassword().equals(password))) {
            return adminUserRepresentation("filler", this.findAdmin(username));
            }
        //if FALSE maybe throw expectation?? or return string to say wrong username or wrong password
        return null; //^
    }

    public Response addSubAdmin(AdministrativeUser head, String username, String email, String password){ // changed return type to response, this allows interaction with controller
        if (head.getIsHead()){
            createAdministrator(username, email, password, false);
            return adminUserRepresentation("filler", this.findAdmin(username));
        } else{
            //return false;
            return null;
        }
    }

    public boolean addSubAdmin(AdministrativeUser head, String username, String email, String telephone, String password){
        if (head.getIsHead()){
            createAdministrator(username, email, telephone, password, false);
            return true;
            // return adminUserRepresentation("filler", this.findAdmin(username));
        } else{
            return false;
            // return null;
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

    public AdministrativeUser findAdmin(String username) {
        /* for (AdministrativeUser i : administrators) { // is there a current way to iterate over Repository without filter? if not i implement it?
            if (username.equals(i.getUserName())) return i;
        }
         */
        return null;
    }

    //public void exampleOfFilter() {
        //Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezed);
        //Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount()); // they are the same
        // this iterator has all PersonalUsers that need to be frozen
    //}


        //public void findFreezeUser() {
        //r}


}

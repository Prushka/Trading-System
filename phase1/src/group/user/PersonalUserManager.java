package group.user;

import group.menu.data.Response;
import group.repository.Filter;
import group.repository.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonalUserManager {
    private static AdministrativeManager am;
    private final Repository<PersonalUser> personalUserRepository;
    //private PersonalUser

    public PersonalUserManager(Repository<PersonalUser> personalUserRepository) {
        //instantiate AdminManager
        this.personalUserRepository = personalUserRepository;
    }

    public void exampleOfFilter() {
        Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount());
        Iterator<PersonalUser> usersToBeFrozen3 = personalUserRepository.iterator(new Filter<PersonalUser>() {
            @Override
            public boolean match(PersonalUser personalUser) {
                return personalUser.getLendCount() < personalUser.getBorrowCount();
            }
        }); // usersToBeFrozen / usersToBeFrozen2 / usersToBeFrozen3 work the same here
        // this iterator has all PersonalUsers that need to be frozen
    }

    public Response verifyLogin(String username, String password){
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username)
                        && PersonalUser.getPassword().equals(password))){
            //currPersonalUser = personalUser.getFirst(
                    //AdministrativeUser -> AdministrativeUser.getUserName().equals(username)
                            //&& AdministrativeUser.getPassword().equals(password));
            return new Response.Builder(true).translatable("success.login.user").build();
        }
        return new Response.Builder(false).translatable("failed.login.user").build();
    }

    public Response createPersonalUser(String userName, String email, String telephone, String password) {
        PersonalUser p = new PersonalUser(userName, email, telephone, password);
        personalUserRepository.add(p);
        return new Response.Builder(true).translatable("success.create.new").build();
    }


    public boolean notifyAdmin(String input, PersonalUser p) { //TODO send the request to admin
        if (input.equalsIgnoreCase("add")) {
            //add a request to add item to the main inventory to the notification list.
        } else if (input.equalsIgnoreCase("unfreeze")) {
            //add a request to unfreeze the user's account to the notification list.
        }
        return false;
    }

    public void addItemRequest(PersonalUser user, Long item){
        //TODO implement
    }

    public void addUnfreezeRequest(PersonalUser user){
        //TODO implement repo or list??
    }


}

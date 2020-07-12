package group.user;

import group.repository.Repository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PersonalUserManager {
    private static final List<PersonalUser> personalusers = new ArrayList<>();
    private static AdministrativeManager am;
    private final Repository<PersonalUser> personalUserRepository;

    public PersonalUserManager(Repository<PersonalUser> personalUserRepository) {
        //instantiate AdminManager
        this.personalUserRepository = personalUserRepository;
    }

    public void exampleOfFilter() {
        Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezed);
        Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount()); // they are the same
        // this iterator has all PersonalUsers that need to be frozen
    }

    public boolean verify(String username, String password) {
        for (PersonalUser p : personalusers) {
            if (p.getName().equals(username) && p.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public boolean notifyAdmin(String input, PersonalUser p) {
        if (input.equalsIgnoreCase("add")) {
            //add a request to add item to the main inventory to the notification list.
        } else if (input.equalsIgnoreCase("unfreeze")) {
            //add a request to unfreeze the user's account to the notification list.
        }
        return false;
    }

    public void createPersonalUser(String userName, String email, String password) {
        PersonalUser p = new PersonalUser(userName, email, password);
        personalusers.add(p);
    }

    public void createPersonalUser(String userName, String email, String telephone, String password) {
        PersonalUser p = new PersonalUser(userName, email, telephone, password);
        personalusers.add(p);
    }


}

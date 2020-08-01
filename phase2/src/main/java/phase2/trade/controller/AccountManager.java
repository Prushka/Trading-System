package phase2.trade.controller;

import phase2.trade.repository.Filter;
import phase2.trade.repository.Repository;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

public class AccountManager {

    private UserRepository userRepo;

    public AccountManager(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(String username, String email, String password) {
        if (userRepo.getPersonal().ifExists(entity -> entity.getUserName().equals(username) || entity.getEmail().equals(email))) {
            return false;
        } else {
            userRepo.getPersonal().add(new PersonalUser(username, email, password));
            return true;
        }
    }
}

package phase2.trade.controller;

import phase2.trade.repository.CSVRepository;
import phase2.trade.repository.Repository;
import phase2.trade.repository.SaveHook;
import phase2.trade.user.AdministrativeUser;
import phase2.trade.user.Guest;
import phase2.trade.user.PersonalUser;

public class UserRepository {

    private final Repository<AdministrativeUser> adminRepo;
    private final Repository<PersonalUser> personalRepo;

    public UserRepository(SaveHook saveHook) {
        adminRepo = new CSVRepository<>(AdministrativeUser.class, "data/admin.csv", saveHook);
        personalRepo = new CSVRepository<>(PersonalUser.class, "data/personal.csv", saveHook);
    }

    public Repository<AdministrativeUser> getAdmin() {
        return adminRepo;
    }

    public Repository<PersonalUser> getPersonal() {
        return personalRepo;
    }
}

package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.AdministrativeManager;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController {

    Repository<PersonalUser> personalRepo;
    Repository<AdministrativeUser> adminRepo;

    private final AdministrativeManager administrativeManager;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        administrativeManager = new AdministrativeManager(adminRepo, personalRepo);
        dispatcher.menuConstructor.user(this);
    }

    public Response loginAdmin(String username, String password) {
        return administrativeManager.verifyLogin(username, password);
    }

    public Response registerUser(String username, String email, String password, boolean isHead) {
        return administrativeManager.createAdministrator(username, email, password, isHead);
    }

}

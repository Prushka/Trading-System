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

    public Response loginAdmin(Request request) {
        return administrativeManager.verifyLogin(request.get("username"), // not sure if this way of using Request is correct
                                                 request.get("password"));
    }

    public Response registerAdmin(Request request) {
        return administrativeManager.createAdministrator(request.get("username"),
                                                         request.get("email"),
                                                         request.get("password"),
                                                        (request.getBoolean("isHead")));
    }

    // working on the rest
}

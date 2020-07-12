package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController {

    Repository<PersonalUser> personalRepo;
    Repository<AdministrativeUser> adminRepo;

    public UserController(ControllerDispatcher dispatcher) {
        dispatcher.menuConstructor.user(this);
    }

    public Response loginUser(Request request) {
        return null;
    }

    public Response registerUser(Request request) {
        return null;
    }

}

package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.RepositorySavable;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController {

    RepositorySavable<PersonalUser> personalRepo;
    RepositorySavable<AdministrativeUser> adminRepo;

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

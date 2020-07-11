package group.system;

import group.repository.Repository;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController {

    Repository<PersonalUser> personalRepo;
    Repository<AdministrativeUser> adminRepo;

    public UserController(ControllerDispatcher controllerDispatcher) {

    }

}

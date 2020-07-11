package group.system;

import group.repository.Repository;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController implements Shutdown {

    Repository<PersonalUser> personalRepo;
    Repository<AdministrativeUser> adminRepo;

    public UserController(ControllerDispatcher controllerDispatcher) {
    }


    @Override
    public void shutdown() {

    }
}

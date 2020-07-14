package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.AdministrativeManager;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class UserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    private final AdministrativeManager administrativeManager;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        administrativeManager = new AdministrativeManager(adminRepo, personalRepo);
        dispatcher.menuConstructor.user(this);
    }

    // Lucy comment: for now it is only for admin, will discuss how to put in personal
    // or separate personal and admin controller
    public Response loginUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        return administrativeManager.verifyLogin(username, password);
    }

    public Response registerUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        boolean isHead = request.getBoolean("isHead");
        return administrativeManager.createadministrator(username, email, telephone, password, isHead);
    }

    public Response addSubAdmin(Request request){
        AdministrativeUser curradmin = administrativeManager.getCurrAdmin();
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        //boolean isHead = request.getBoolean("isHead");
        return administrativeManager.addSubAdmin(curradmin, username, email, telephone, password);
    }

    //public Response

}

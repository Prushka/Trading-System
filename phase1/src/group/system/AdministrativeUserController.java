package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.AdministrativeManager;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

import java.util.Iterator;

public class AdministrativeUserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    private final AdministrativeManager administrativeManager;
    private AdministrativeUser currAdmin;

    public AdministrativeUserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        administrativeManager = new AdministrativeManager(adminRepo, personalRepo);
        dispatcher.menuConstructor.adminUser(this);
    }

    // Lucy comment: for now it is only for admin, will discuss how to put in personal
    // or separate personal and admin controller
    public Response loginAdminUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        currAdmin = administrativeManager.getCurrAdmin(username, password);
        return administrativeManager.verifyLogin(username, password);
    }

    public Response registerAdminUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        boolean isHead = request.getBoolean("isHead");
        return administrativeManager.createAdministrator(username, email, telephone, password, isHead);
    }

    public Response addSubAdmin(Request request){
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        //boolean isHead = request.getBoolean("isHead");
        return administrativeManager.addSubAdmin(currAdmin, username, email, telephone, password);
    }

    public Iterator<PersonalUser> freezeUser(Request request){
        return administrativeManager.getListUserShouldBeFreezed();
    }




}

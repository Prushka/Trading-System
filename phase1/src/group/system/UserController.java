package group.system;

import group.menu.data.Request;
import group.menu.data.Response;
import group.repository.Repository;
import group.user.*;

import java.util.Iterator;

public class UserController {

    private final Repository<PersonalUser> personalRepo;
    private final Repository<AdministrativeUser> adminRepo;
    private final PersonalUserManager personalUserManager;
    private PersonalUser currUser;

    public UserController(ControllerDispatcher dispatcher) {
        personalRepo = dispatcher.personalUserRepository;
        adminRepo = dispatcher.adminUserRepository;
        personalUserManager = new PersonalUserManager(personalRepo);
        dispatcher.menuConstructor.user(this);
    }

    // Lucy comment: for now it is only for admin, will discuss how to put in personal
    // or separate personal and admin controller
    public Response loginUser(Request request) {
        String username = request.get("username");
        String password = request.get("password");
        currUser= personalUserManager.getCurrPersonalUser(username, password);
        return personalUserManager.verifyLogin(username,password);
    }

    public Response registerUser(Request request) {
        String username = request.get("username");
        String email = request.get("email");
        String telephone = request.get("telephone");
        String password = request.get("password");
        return personalUserManager.createPersonalUser(username, email, telephone, password);
    }

    public Response RequestAddNewItem(Request request){
        String item = request.get("item");
        String description = request.get("description");
        return personalUserManager.createNewItemAndRequestAdd(currUser, item, description);
    }

    public Response RequestUnfreeze(Request request) {
        return personalUserManager.UnfreezeRequest(currUser);
    }


    //public Response addSubAdmin(Request request){
       // AdministrativeUser curradmin = administrativeManager.getCurrAdmin();
        //String username = request.get("username");
       // String email = request.get("email");
        //String telephone = request.get("telephone");
        //String password = request.get("password");
        //boolean isHead = request.getBoolean("isHead");
        //return administrativeManager.addSubAdmin(curradmin, username, email, telephone, password);
    //}


}

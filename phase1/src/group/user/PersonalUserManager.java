package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Filter;
import group.repository.Repository;

import java.util.*;

public class PersonalUserManager {
    //private static AdministrativeManager am;
    private final Repository<PersonalUser> personalUserRepository;
    private PersonalUser currPersonalUser = null;


    public PersonalUserManager(Repository<PersonalUser> personalUserRepository) {
        //instantiate AdminManager
        this.personalUserRepository = personalUserRepository;

    }

    public void exampleOfFilter() {
        Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount());
        Iterator<PersonalUser> usersToBeFrozen3 = personalUserRepository.iterator(new Filter<PersonalUser>() {
            @Override
            public boolean match(PersonalUser personalUser) {
                return personalUser.getLendCount() < personalUser.getBorrowCount();
            }
        }); // usersToBeFrozen / usersToBeFrozen2 / usersToBeFrozen3 work the same here
        // this iterator has all PersonalUsers that need to be frozen
    }

    public Response verifyLogin(String username, String password){
        if (getCurrPersonalUser(username, password) != null){
            return new Response.Builder(true).translatable("success.login.user").build();
        }
        return new Response.Builder(false).translatable("failed.login.user").build();
    }

    public PersonalUser getCurrPersonalUser(String username, String password) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username)
                        && PersonalUser.getPassword().equals(password))) {
            currPersonalUser = personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username)
                            && PersonalUser.getPassword().equals(password));
        }
        return currPersonalUser;
    }

    public Response createPersonalUser(String userName, String email, String telephone, String password) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(userName))){
            return new Response.Builder(false).translatable("failed.create.new").build();
        }
        PersonalUser p = new PersonalUser(userName, email, telephone, password);
        personalUserRepository.add(p);
        return new Response.Builder(true).translatable("success.create.new").build();
    }

    public Response requestToAddItemToInventory(PersonalUser user, Long item){
        user.addItemToAddToInventoryRequest(item);
        return new Response.Builder(true).translatable("success.request.addItem").build(); // this one
    }

    public Response UnfreezeRequest(PersonalUser user){
        user.setRequestToUnfreeze(true);
        return new Response.Builder(true).translatable("success.request.unfreeze").build();
    }

    public Response createNewItemAndRequestAdd(PersonalUser owner, String item, String description){
        Item newitem = new Item(owner.getUid(), item, description);
        return requestToAddItemToInventory(owner, newitem.getUid());
    }

    public Response removeItemFromInventory(PersonalUser user, Long item) {
        user.removeFromInventory(item);
        return new Response.Builder(true).translatable("success.remove.item").build();
    }

    public Response addItemToWishlist(PersonalUser user, Long item){
        user.addToWishList(item);
        return new Response.Builder(true).translatable("success.add.wishlist").build(); // this one
    }

    public Response removeItemFromWishlist(PersonalUser user, Long item){
        user.removeFromWishList(item);
        return new Response.Builder(true).translatable("success.remove.wishlist").build();
    }

    public Response getUserInventory(PersonalUser user){
        return new Response.Builder(true).
                translatable("success.get.inventory", user.getInventory()).build();
    }

    public Response getUserWishlist(PersonalUser user){
        return new Response.Builder(true).
                translatable("success.get.wishlist", user.getWishlist()).build();
    }

    public Response getUserIsFrozen(PersonalUser user){
        if (user.getIsFrozen()){
            return new Response.Builder(true).translatable("true.is.frozen", user.getIsFrozen()).build();
        }else{
            return new Response.Builder(false).translatable("false.is.frozen", user.getIsFrozen()).build();
        }
    }



}

package group.user;

import group.item.Item;
import group.menu.data.Response;
import group.repository.Filter;
import group.repository.Repository;

import java.util.*;

public class PersonalUserManager {

    private final Repository<PersonalUser> personalUserRepository;
    private PersonalUser currPersonalUser = null;
    private final Repository<Item> itemRepository;



    public PersonalUserManager(Repository<PersonalUser> personalUserRepository, Repository<Item> itemRepository) {
        this.personalUserRepository = personalUserRepository;
        this.itemRepository = itemRepository;

    }

    /*public void exampleOfFilter() {
        Iterator<PersonalUser> usersToBeFrozen = personalUserRepository.iterator(PersonalUser::getShouldBeFreezedUser);
        Iterator<PersonalUser> usersToBeFrozen2 = personalUserRepository.iterator(personalUser -> personalUser.getLendCount() < personalUser.getBorrowCount());
        Iterator<PersonalUser> usersToBeFrozen3 = personalUserRepository.iterator(new Filter<PersonalUser>() {
            @Override
            public boolean match(PersonalUser personalUser) {
                return personalUser.getLendCount() < personalUser.getBorrowCount();
            }
        }); // usersToBeFrozen / usersToBeFrozen2 / usersToBeFrozen3 work the same here
        // this iterator has all PersonalUsers that need to be frozen
    }*/

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

    public Response requestToAddItemToInventory(PersonalUser user, String item, String description){
        Item newItem = new Item(user.getUid(), item, description);
        itemRepository.add(newItem);
        user.addItemToAddToInventoryRequest(newItem.getUid());
        return new Response.Builder(true).translatable("success.request.addItem").build();
    }

    public Response UnfreezeRequest(PersonalUser user){
        user.setRequestToUnfreeze(true);
        return new Response.Builder(true).translatable("success.request.unfreeze").build();
    }

    /*public Response createNewItemAndRequestAdd(PersonalUser owner, String item, String description){
        Item newitem = new Item(owner.getUid(), item, description);
        return requestToAddItemToInventory(owner, newitem.getUid());
    }*/

    public Response removeItemFromInventory(PersonalUser user, Integer item) {
        user.removeFromInventory(item);
        itemRepository.remove(item);
        return new Response.Builder(true).translatable("success.remove.item").build();
    }

    public Response addItemToWishlist(PersonalUser user, String item, String description){
        Integer uid = createNewItem(user.getUid(), item, description);
        user.addToWishList(uid);
        return new Response.Builder(true).translatable("success.add.wishlist").build();
    }

    public Response removeItemFromWishlist(PersonalUser user, Item item){
        user.removeFromWishList(item.getUid());
        itemRepository.remove(item);
        return new Response.Builder(true).translatable("success.remove.wishlist").build();
    }

    public List<Integer> getUserInventory(PersonalUser user){
        return user.getInventory();
    }

    public List<Integer> getUserWishlist(PersonalUser user){
        return user.getWishlist();
    }

    public Response getUserIsFrozen(PersonalUser user){
        if (user.getIsFrozen()){
            return new Response.Builder(true).translatable("true.is.frozen", user.getIsFrozen()).build();
        }else{
            return new Response.Builder(false).translatable("false.is.frozen", user.getIsFrozen()).build();
        }
    }

    public Integer createNewItem(Integer ownerUID, String item, String description){
        Item newItem = new Item(ownerUID, item, description);
        itemRepository.add(newItem);
        return newItem.getUid();
    }
}

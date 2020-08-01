package main.java.com.phase2.trade.user;

import main.java.com.phase2.trade.item.*;
import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;

import java.util.*;

public class PersonalUserManager {

    private final Repository<PersonalUser> personalUserRepository;
    private PersonalUser currPersonalUser;
    private final Repository<Item> itemRepository;
    private PersonalUser currUser;


    public PersonalUserManager(Repository<PersonalUser> personalUserRepository, Repository<Item> itemRepository) {
        this.personalUserRepository = personalUserRepository;
        this.itemRepository = itemRepository;

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

    public boolean verifyLogin(String username, String password){
        return getCurrPersonalUser(username, password) != null;
    }

    public PersonalUser getCurrPersonalUser(String username, String password) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(username)
                        && PersonalUser.getPassword().equals(password))) {
            currPersonalUser = personalUserRepository.getFirst(
                    PersonalUser -> PersonalUser.getUserName().equals(username)
                            && PersonalUser.getPassword().equals(password));
        }
        currUser = currPersonalUser;
        return currPersonalUser;
    }

    public boolean createPersonalUser(String userName, String email, String telephone, String password) {
        if (personalUserRepository.ifExists(
                PersonalUser -> PersonalUser.getUserName().equals(userName))) {
            return false; // new Response.Builder(false).translatable("failed.create.new").build();
        }
        PersonalUser p = new PersonalUser(userName, email, telephone, password);
        personalUserRepository.add(p);
        return true;// new Response.Builder(true).translatable("success.create.new").build();
    }

    public boolean requestToAddItemToInventory(PersonalUser user, String item, String description) {
        Item newItem = new Item(user.getUid(), item, description);
        itemRepository.add(newItem);
        user.addItemToAddToInventoryRequest(newItem.getUid());
        return true;
    }

    public boolean requestToAddItemToInventory(PersonalUser user, Integer item){
        user.addItemToAddToInventoryRequest(item);
        return true;//new Response.Builder(true).translatable("success.request.addItem").build();
    }

    public boolean UnfreezeRequest(PersonalUser user){
        user.setRequestToUnfreeze(true);
        return true;//new Response.Builder(true).translatable("success.request.unfreeze").build();
    }

    public boolean createNewItemAndRequestAdd(PersonalUser owner, String item, String description){
        Item newitem = new Item(owner, item, description);
        return requestToAddItemToInventory(owner, newitem.getUid());
    }

    public boolean removeItemFromInventory(PersonalUser user, Integer item){
        user.removeFromInventory(item);
        return true;//new Response.Builder(true).translatable("success.remove.item").build();
    }

    public boolean addItemToWishlist(PersonalUser user, String item, String description) {
        Integer uid = createNewItem(user.getUid(), item, description);
        user.addToWishList(uid);
        return true;//new Response.Builder(true).translatable("success.add.wishlist").build();
    }

    public boolean removeItemFromWishlist(PersonalUser user, Item item) {
        user.removeFromWishList(item.getUid());
        itemRepository.remove(item);
        return true;//new Response.Builder(true).translatable("success.remove.wishlist").build();
    }

//    public List<Integer> getUserInventory(PersonalUser user) {
//        return user.getInventory();
//    }
//
//    public List<Integer> getUserWishlist(PersonalUser user) {
//        return user.getWishlist();
//    }

    public boolean getUserIsFrozen(PersonalUser user) {
        if (user.getIsFrozen()) {
            return true;//new Response.Builder(true).translatable("true.is.frozen", user.getIsFrozen()).build();
        } else {
            return false;//new Response.Builder(false).translatable("false.is.frozen", user.getIsFrozen()).build();
        }
    }

    public Integer createNewItem(Integer ownerUID, String item, String description) {
        Item newItem = new Item(ownerUID, item, description);
        itemRepository.add(newItem);
        return newItem.getUid();
    }

    public void suggest (PersonalUser p) {
        for (PersonalUser x : personalUserRepository) {
            for (Item i : x.getInventory()) {
                if (p.getWishlist().contains(i)) {
                    p.suggest(i);
                }
            }
        }
    }

    public PersonalUser getCurrUser() {
        return currUser;
    }
}

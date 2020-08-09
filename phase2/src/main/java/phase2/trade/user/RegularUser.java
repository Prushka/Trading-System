package phase2.trade.user;

import phase2.trade.inventory.Cart;
import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.ItemListType;
import phase2.trade.inventory.ItemList;
import phase2.trade.permission.PermissionGroup;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class RegularUser extends User {

    private AccountState accountState;

    @OneToOne(cascade = CascadeType.ALL)
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    private int accountBalance;

    //private int incompleteTrades;

    /**
     * Creates a PersonalUser with the given userName, email, telephone, password
     * and initializes all other instance variables.
     *
     * @param userName username of this user
     * @param email    email of this user
     * @param password password of this user
     */
    public RegularUser(String userName, String email, String password, String country, String city) {
        super(userName, email, password, country, city);
        accountState = AccountState.NORMAL;
        //incompleteTrades = 0;

        inventory = new Inventory();
        inventory.setOwner(this);

        cart = new Cart();
        cart.setOwner(this);

        this.accountBalance = 0;
    }

    public RegularUser() {

    }

    //public int getIncompleteTrades() {
    //return incompleteTrades;
    //}


    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public ItemList getItemList(ItemListType itemListType) {
        switch (itemListType) {
            case CART:
                return cart;
            case INVENTORY:
                return inventory;
        }
        return null;
    }

    public int getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(int accountBalance) {
        this.accountBalance = accountBalance;
    }

    public PermissionGroup getPermissionGroup() {
        return PermissionGroup.REGULAR;
    }
}

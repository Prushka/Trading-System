package phase2.trade.user;


import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.Cart;
import phase2.trade.trade.Trade;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true, length = 40)
    private String userName;

    @Column(unique = true, length = 40)
    private String email;
    private String telephone;
    private String password;

    @Embedded
    private Address address;

    @Embedded
    private UserPermission userPermission;

    @OneToOne(cascade = CascadeType.ALL)
    private Inventory inventory;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Trade> listOfTrades;

    // using a map may add some polymorphism but will complicate the db structure

    private Integer reputation;

    private Integer point;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName the username of this Person.
     * @param email    the email this Person.
     * @param password the password this user set to
     */
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;

        inventory = new Inventory();
        inventory.setOwner(this);

        cart = new Cart();
        cart.setOwner(this);

    }

    public User() {

    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getUid() {
        return uid;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
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

    public ItemList getItemList(InventoryType inventoryType) {
        switch (inventoryType) {
            case CART:
                return cart;
            case INVENTORY:
                return inventory;
        }
        return null;
    }

    public List<Trade> getListOfTrades() {
        return listOfTrades;
    }

    public void setListOfTrades(List<Trade> listOfTrade) {
        this.listOfTrades = listOfTrade;
    }

    public Integer getReputation() {
        return reputation;
    }

    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public UserPermission getUserPermission() {
        return userPermission;
    }

    public void setUserPermission(UserPermission permission) {
        this.userPermission = permission;
    }

    public boolean hasPermission(Permission permission) {
        return userPermission.hasPermission(permission);
    }
}


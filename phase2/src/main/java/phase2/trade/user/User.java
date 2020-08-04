package phase2.trade.user;


import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.InventoryType;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.Cart;
import phase2.trade.item.Item;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

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

    @OneToMany(cascade = CascadeType.ALL)
    private Map<InventoryType, ItemList> itemListMap = new HashMap<>();


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

        Inventory inventory = new Inventory();
        inventory.setOwner(this);

        Cart cart = new Cart();
        cart.setOwner(this);

        itemListMap.put(InventoryType.INVENTORY, cart);
        itemListMap.put(InventoryType.CART, inventory);
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

    public abstract boolean isAdmin();

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<InventoryType, ItemList> getItemListMap() {
        return itemListMap;
    }

    public void setItemListMap(Map<InventoryType, ItemList> itemListMap) {
        this.itemListMap = itemListMap;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}


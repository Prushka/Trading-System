package phase2.trade.user;


import phase2.trade.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToMany(cascade=CascadeType.ALL)
    private List<Item> inventory;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Item> wishToBorrowList;

    @OneToMany(cascade=CascadeType.ALL)
    private List<Item> wishToLendList;


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
        this.inventory = new ArrayList<>();
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

    public List<Item> getInventory() {
        return inventory;
    }

    public void setInventory(List<Item> items) {
        this.inventory = items;
    }

    public void addItem(Item item){
        this.inventory.add(item);
        item.setOwner(this);
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Item> getWishToBorrowList() {
        return wishToBorrowList;
    }

    public void setWishToBorrowList(List<Item> wishToBorrowList) {
        this.wishToBorrowList = wishToBorrowList;
    }

    public List<Item> getWishToLendList() {
        return wishToLendList;
    }

    public void setWishToLendList(List<Item> wishToLendList) {
        this.wishToLendList = wishToLendList;
    }
}


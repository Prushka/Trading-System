package phase2.trade.user;

import phase2.trade.address.AddressBook;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;

import javax.persistence.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    private AddressBook addressBook;

    @Embedded
    private PermissionSet permissionSet;

    private PermissionGroup permissionGroup;

    // using a map may add some polymorphism but will complicate the db structure

    private Integer reputation;

    private Integer point;

    private String city;

    private String country;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName the username of this Person.
     * @param email    the email this Person.
     * @param password the password this user set to
     */
    public User(String userName, String email, String password, String country, String city) {
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.addressBook = new AddressBook();
        this.reputation = 0;
        this.point = 0;
        this.country = country;
        this.city = city;

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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
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

    public PermissionSet getPermissionSet() {
        return permissionSet;
    }

    public void setUserPermission(PermissionSet permission) {
        this.permissionSet = permission;
    }

    public boolean hasPermission(Permission permission) {
        return getPermissionSet().hasPermission(permission);
    }

    public AddressBook getAddressBook() {
        return addressBook;
    }

    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    public String getCountry(){
        return country;
    }

    public void  setCountry(String country){
        this.country = country;
    }

    public String getCity(){
        return city;
    }

    public void  setCity(String city){
        this.city = city;
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public ItemList getItemList(ItemListType itemListType) { // this should not happen!
        throw new IllegalArgumentException("User: "+ userName + " is not a Regular User but is trying to get his/her ItemList");
    }
}


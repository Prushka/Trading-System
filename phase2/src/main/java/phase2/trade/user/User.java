package phase2.trade.user;

import phase2.trade.address.AddressBook;
import phase2.trade.avatar.Avatar;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.permission.PermissionSet;
import phase2.trade.support.SupportTicket;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

// TODO: even if you make AdministrativeUser have REGULAR Permission Group. He/She won't be able to trade
//  Simply because AdministrativeUser doesn't have any ItemList
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(unique = true, length = 40)
    private String name;

    @Column(unique = true, length = 40)
    private String email;
    private String phoneNumber;
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressBook addressBook;

    private AccountState accountState;

    @Embedded
    private PermissionSet permissionSet;

    private PermissionGroup permissionGroup;

    private Integer reputation;

    private Integer point;

    private String city;

    private String country;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<SupportTicket> supportTickets;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Avatar avatar;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param name     the username of this Person.
     * @param email    the email this Person.
     * @param password the password this user set to
     */
    public User(String name, String email, String password, String country, String city) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.addressBook = new AddressBook();
        this.reputation = 0;
        this.point = 0;
        this.country = country;
        this.city = city;

        accountState = AccountState.NORMAL;
        this.supportTickets = new HashSet<>();
    }

    public User() {

    }


    public AccountState getAccountState() {
        return accountState;
    }

    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String telephone) {
        this.phoneNumber = telephone;
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

    public void setName(String userName) {
        this.name = userName;
    }

    public String getName() {
        return name;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public ItemList getItemList(ItemListType itemListType) { // this should not happen!
        throw new IllegalArgumentException("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
    }

    public ItemList getInventory() { // this should not happen!
        return getItemList(ItemListType.INVENTORY);
    }

    public ItemList getCart() { // this should not happen!
        return getItemList(ItemListType.CART);
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Set<SupportTicket> getSupportTickets() {
        return supportTickets;
    }

    public void setSupportTickets(Set<SupportTicket> supportSupportTickets) {
        this.supportTickets = supportSupportTickets;
    }
}


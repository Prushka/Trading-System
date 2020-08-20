package phase2.trade.user;

import phase2.trade.address.AddressBook;
import phase2.trade.avatar.Avatar;
import phase2.trade.exception.IllegalUserOperationException;
import phase2.trade.itemlist.Cart;
import phase2.trade.itemlist.Inventory;
import phase2.trade.itemlist.ItemList;
import phase2.trade.itemlist.ItemListType;
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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<SupportTicket> supportTickets;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Avatar avatar;

    public User(String userName, String email, String password) {
        this.name = userName;
        this.email = email;
        this.password = password;
        this.addressBook = new AddressBook();
        this.reputation = 0;
        this.point = 0;

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

    public void setEmail(String email) {
        this.email = email;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String userName) {
        this.name = userName;
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

    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    public ItemList getItemList(ItemListType itemListType) { // this should not happen!
        throw new IllegalUserOperationException("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
    }

    public Inventory getInventory() { // this should not happen!
        throw new IllegalUserOperationException("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
    }

    public Cart getCart() {
        throw new IllegalUserOperationException("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
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

    public void lazyLoad() {
        // A better approach is to be discovered
        // This method is used to optimize database query and load necessary fields from database when used
        // For example, the Item's owner is a User, thus, fetching an Item will cause the corresponding User (and all its non-lazy fields) to be selected
        // (Since the Item's owner's address is useful in marketplace)
        // this slows the load speed of Market Items and may potentially be an expensive operation
        // (To get 4 items without lazy loading: 0.0552422 seconds compared to 0.0407408 seconds with lazy loading)
        // By making most fields lazy and define a lazyLoad method in all Users, necessary fields will be selected automatically by hibernate when
        // this User is used at that point (when a Command that gets this entity calls lazyLoad() in an opened session, these fields will then be selected)
        int size = getSupportTickets().size();
        if (getAvatar() != null) {
            getAvatar().getImageData(); // lazy load
        }

    }
}


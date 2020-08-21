package phase2.trade.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.address.AddressBook;
import phase2.trade.avatar.Avatar;
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

/**
 * The User.
 *
 * @author Dan Lyu
 * @author Lingyun Wang
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

// TODO: even if you make AdministrativeUser have REGULAR Permission Group. He/She won't be able to trade
//  Simply because AdministrativeUser doesn't have any ItemList
public abstract class User {

    private static final Logger logger = LogManager.getLogger(User.class);


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

    /**
     * Constructs a new User.
     *
     * @param userName the user name
     * @param email    the email
     * @param password the password
     */
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

    /**
     * Constructs a new User.
     */
    public User() {

    }


    /**
     * Gets account state.
     *
     * @return the account state
     */
    public AccountState getAccountState() {
        return accountState;
    }

    /**
     * Sets account state.
     *
     * @param accountState the account state
     */
    public void setAccountState(AccountState accountState) {
        this.accountState = accountState;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param telephone the telephone
     */
    public void setPhoneNumber(String telephone) {
        this.phoneNumber = telephone;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param userName the user name
     */
    public void setName(String userName) {
        this.name = userName;
    }

    /**
     * Gets reputation.
     *
     * @return the reputation
     */
    public Integer getReputation() {
        return reputation;
    }

    /**
     * Sets reputation.
     *
     * @param reputation the reputation
     */
    public void setReputation(Integer reputation) {
        this.reputation = reputation;
    }

    /**
     * Gets point.
     *
     * @return the point
     */
    public Integer getPoint() {
        return point;
    }

    /**
     * Sets point.
     *
     * @param point the point
     */
    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * Gets permission set.
     *
     * @return the permission set
     */
    public PermissionSet getPermissionSet() {
        return permissionSet;
    }

    /**
     * Sets user permission.
     *
     * @param permission the permission
     */
    public void setUserPermission(PermissionSet permission) {
        this.permissionSet = permission;
    }

    /**
     * Has permission boolean.
     *
     * @param permission the permission
     * @return the boolean
     */
    public boolean hasPermission(Permission permission) {
        return getPermissionSet().hasPermission(permission);
    }

    /**
     * Gets address book.
     *
     * @return the address book
     */
    public AddressBook getAddressBook() {
        return addressBook;
    }

    /**
     * Sets address book.
     *
     * @param addressBook the address book
     */
    public void setAddressBook(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Gets permission group.
     *
     * @return the permission group
     */
    public PermissionGroup getPermissionGroup() {
        return permissionGroup;
    }

    /**
     * Sets permission group.
     *
     * @param permissionGroup the permission group
     */
    public void setPermissionGroup(PermissionGroup permissionGroup) {
        this.permissionGroup = permissionGroup;
    }

    /**
     * Gets item list.
     *
     * @param itemListType the item list type
     * @return the item list
     */
    public ItemList getItemList(ItemListType itemListType) { // this should not happen!
        logger.warn("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
        return new Inventory();
    }

    /**
     * Gets inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() { // this should not happen!
        logger.warn("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
        return new Inventory();
    }

    /**
     * Gets cart.
     *
     * @return the cart
     */
    public Cart getCart() {
        logger.warn("User: " + name + " is not a Regular User but is trying to get his/her ItemList");
        return new Cart();
    }

    /**
     * Gets avatar.
     *
     * @return the avatar
     */
    public Avatar getAvatar() {
        return avatar;
    }

    /**
     * Sets avatar.
     *
     * @param avatar the avatar
     */
    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * Gets support tickets.
     *
     * @return the support tickets
     */
    public Set<SupportTicket> getSupportTickets() {
        return supportTickets;
    }

    /**
     * Sets support tickets.
     *
     * @param supportSupportTickets the support support tickets
     */
    public void setSupportTickets(Set<SupportTicket> supportSupportTickets) {
        this.supportTickets = supportSupportTickets;
    }

    /**
     * Lazy load.
     */
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


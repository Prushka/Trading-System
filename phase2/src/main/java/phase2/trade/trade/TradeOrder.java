package phase2.trade.trade;

import phase2.trade.address.Address;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * The Trade order.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TradeOrder {

    private Long uid;

    private UserOrderBundle left;

    private UserOrderBundle right;

    private LocalDateTime dateAndTime;

    private Address addressTrade;

    private Address addressTradeBack;

    private OrderState orderState;

    /**
     * Gets uid.
     *
     * @return the uid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
     * Gets left bundle.
     *
     * @return the left bundle
     */
    @OneToOne(cascade = CascadeType.ALL)
    public UserOrderBundle getLeftBundle() {
        return left;
    }

    /**
     * Sets left bundle.
     *
     * @param left the left
     */
    public void setLeftBundle(UserOrderBundle left) {
        this.left = left;
    }

    /**
     * Gets right bundle.
     *
     * @return the right bundle
     */
    @OneToOne(cascade = CascadeType.ALL)
    public UserOrderBundle getRightBundle() {
        return right;
    }

    /**
     * Sets right bundle.
     *
     * @param right the right
     */
    public void setRightBundle(UserOrderBundle right) {
        this.right = right;
    }

    /**
     * Gets right user.
     *
     * @return the right user
     */
    @Transient
    public User getRightUser() {
        return getRightBundle().getUser();
    }

    /**
     * Gets left user.
     *
     * @return the left user
     */
    @Transient
    public User getLeftUser() {
        return getLeftBundle().getUser();
    }


    /**
     * Gets date and time.
     *
     * @return the date and time
     */
    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    /**
     * Sets date and time.
     *
     * @param dateAndTime the date and time
     */
    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    /**
     * Gets address trade.
     *
     * @return the address trade
     */
    @OneToOne(cascade = {CascadeType.ALL})
    public Address getAddressTrade() {
        return addressTrade;
    }

    /**
     * Sets address trade.
     *
     * @param addressTrade the address trade
     */
    public void setAddressTrade(Address addressTrade) {
        this.addressTrade = addressTrade;
    }

    /**
     * Gets address trade back.
     *
     * @return the address trade back
     */
    @OneToOne(cascade = {CascadeType.ALL})
    public Address getAddressTradeBack() {
        return addressTradeBack;
    }

    /**
     * Sets address trade back.
     *
     * @param addressTradeBack the address trade back
     */
    public void setAddressTradeBack(Address addressTradeBack) {
        this.addressTradeBack = addressTradeBack;
    }

    /**
     * Gets order state.
     *
     * @return the order state
     */
    public OrderState getOrderState() {
        return orderState;
    }

    /**
     * Sets order state.
     *
     * @param orderState the order state
     */
    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }


    /**
     * If user pair match order boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    public boolean ifUserPairMatchOrder(User a, User b) { // ids are used to avoid detached entities caused by hibernate (that may not point to the same entity object)
        return a.getUid().equals(getLeftUser().getUid()) && b.getUid().equals(getRightUser().getUid()) ||
                b.getUid().equals(getLeftUser().getUid()) && a.getUid().equals(getRightUser().getUid());
    }

    /**
     * Find bundle by user user order bundle.
     *
     * @param user the user
     * @return the user order bundle
     */
    public UserOrderBundle findBundleByUser(User user) {
        if (user.getUid().equals(getLeftUser().getUid())) {
            return getLeftBundle();
        }
        if (user.getUid().equals(getRightUser().getUid())) {
            return getRightBundle();
        }
        return null;
    }

    /**
     * Find counter bundle by user user order bundle.
     *
     * @param user the user
     * @return the user order bundle
     */
    public UserOrderBundle findCounterBundleByUser(User user) {
        if (user.getUid().equals(getLeftUser().getUid())) {
            return getRightBundle();
        }
        if (user.getUid().equals(getRightUser().getUid())) {
            return getLeftBundle();
        }
        return null;
    }

    /**
     * If user in order boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean ifUserInOrder(User user) {
        return findBundleByUser(user) != null;
    }

    /**
     * Update state.
     */
    public void updateState() {
        if (getRightBundle().isTradeConfirmed() && getLeftBundle().isTradeConfirmed()) {
            setOrderState(OrderState.PENDING_TRADE);
        }
        if (getRightBundle().isTransactionConfirmed() && getLeftBundle().isTransactionConfirmed()) {
            setOrderState(OrderState.CLOSED);
            if (getOrderState() != OrderState.PENDING_TRADE_BACK &&
                    (getRightBundle().getTradeItemHolder().getLendCount() > 0 || getLeftBundle().getTradeItemHolder().getLendCount() > 0)) { // one of the items in this trade has to be returned
                setOrderState(OrderState.PENDING_TRADE_BACK);
                resetEdits();
            }
            if (getRightBundle().isTransactionBackConfirmed() && getLeftBundle().isTransactionBackConfirmed()) {
                setOrderState(OrderState.CLOSED);
            }
        }
    }

    private void resetEdits() {
        getLeftBundle().setEdits(0);
        getRightBundle().setEdits(0);
    }
    /*
    @Transient
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserOrderBundle user : traders) {
            users.add(user.getUser());
        }
        return users;
    }

    public boolean borrowed(User currUser) {
        for (UserOrderBundle user : traders) {
            if (user.getUser().equals(currUser) && !user.getTradeItemHolder().getSetOfItems().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean lent(User currUser) {
        for (UserOrderBundle user : traders) {
            if (!user.getUser().equals(currUser)) {
                for (Item item : user.getTradeItemHolder().getSetOfItems()) {
                    if (((RegularUser) currUser).getInventory().getSetOfItems().containsUid(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }*/
}

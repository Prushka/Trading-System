package phase2.trade.trade;

import phase2.trade.address.Address;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }


    @OneToOne(cascade = CascadeType.ALL)
    public UserOrderBundle getLeftBundle() {
        return left;
    }

    public void setLeftBundle(UserOrderBundle left) {
        this.left = left;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public UserOrderBundle getRightBundle() {
        return right;
    }

    public void setRightBundle(UserOrderBundle right) {
        this.right = right;
    }

    @Transient
    public User getRightUser() {
        return getRightBundle().getUser();
    }

    @Transient
    public User getLeftUser() {
        return getLeftBundle().getUser();
    }


    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    public Address getAddressTrade() {
        return addressTrade;
    }

    public void setAddressTrade(Address addressTrade) {
        this.addressTrade = addressTrade;
    }

    @OneToOne(cascade = {CascadeType.ALL})
    public Address getAddressTradeBack() {
        return addressTradeBack;
    }

    public void setAddressTradeBack(Address addressTradeBack) {
        this.addressTradeBack = addressTradeBack;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }


    public boolean ifUserPairMatchOrder(User a, User b) { // ids are used to avoid detached entities caused by hibernate (that may not point to the same entity object)
        return a.getUid().equals(getLeftUser().getUid()) && b.getUid().equals(getRightUser().getUid()) ||
                b.getUid().equals(getLeftUser().getUid()) && a.getUid().equals(getRightUser().getUid());
    }

    public UserOrderBundle findBundleByUser(User user) {
        if (user.getUid().equals(getLeftUser().getUid())) {
            return getLeftBundle();
        }
        if (user.getUid().equals(getRightUser().getUid())) {
            return getRightBundle();
        }
        return null;
    }

    public UserOrderBundle findCounterBundleByUser(User user) {
        if (user.getUid().equals(getLeftUser().getUid())) {
            return getRightBundle();
        }
        if (user.getUid().equals(getRightUser().getUid())) {
            return getLeftBundle();
        }
        return null;
    }

    public boolean ifUserInOrder(User user) {
        return findBundleByUser(user) != null;
    }

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

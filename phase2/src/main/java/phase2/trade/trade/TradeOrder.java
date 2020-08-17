package phase2.trade.trade;

import phase2.trade.address.Address;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TradeOrder {

    private Long uid;

    private UserOrderBundle initiator;

    private UserOrderBundle target;

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


    @OneToOne
    public UserOrderBundle getInitiator() {
        return initiator;
    }

    public void setInitiator(UserOrderBundle initiator) {
        this.initiator = initiator;
    }

    @OneToOne
    public UserOrderBundle getTarget() {
        return target;
    }

    @Transient
    public User getTargetUser() {
        return getTarget().getUser();
    }

    @Transient
    public User getInitiatorUser() {
        return getInitiator().getUser();
    }

    public void setTarget(UserOrderBundle target) {
        this.target = target;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @OneToOne
    public Address getAddressTrade() {
        return addressTrade;
    }

    public void setAddressTrade(Address addressTrade) {
        this.addressTrade = addressTrade;
    }

    @OneToOne
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
                    if (((RegularUser) currUser).getInventory().getSetOfItems().contains(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }*/
}

package phase2.trade.trade;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    private UserOrderBundle initiator;

    @OneToOne
    private UserOrderBundle target;

    private LocalDateTime dateAndTime;

    private OrderState orderState;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public UserOrderBundle getInitiator() {
        return initiator;
    }

    public void setInitiator(UserOrderBundle initiator) {
        this.initiator = initiator;
    }

    public UserOrderBundle getTarget() {
        return target;
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

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}

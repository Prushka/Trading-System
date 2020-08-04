package phase2.trade.trade;

import phase2.trade.inventory.TradeItemHolder;
import phase2.trade.user.Address;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne
    private User initiator;

    @ManyToOne
    private User target;

    @OneToOne
    private TradeItemHolder tradeItemHolder;

    private LocalDateTime dateAndTime;

    @Embedded
    private Address location;

    private OrderState orderState;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public User getInitiator() {
        return initiator;
    }

    public void setInitiator(User initiator) {
        this.initiator = initiator;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    public TradeItemHolder getTradeItemHolder() {
        return tradeItemHolder;
    }

    public void setTradeItemHolder(TradeItemHolder tradeItemHolder) {
        this.tradeItemHolder = tradeItemHolder;
    }
}

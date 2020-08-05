package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import java.util.*;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany
    private List<Order> orders;

    /**
     * Represents a trade
     */
    public Trade() {}

    /**
     * @return The ID of this trade
     */
    Long getUid() { return uid; }

    List<Order> getOrders(){ return orders; }

    /**
     * @param uid New trade ID
     */
    void setUid(Long uid) { this.uid = uid; }

    void setOrders(List<Order> orders){ this.orders = orders;}

    abstract Tradable getStrategy();
    abstract void setStrategy(Tradable newStrategy);
}
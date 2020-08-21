package phase2.trade.trade;

import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * The Trade.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<TradeOrder> orders = new HashSet<>();

    private LocalDateTime localDateTime;

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Gets orders.
     *
     * @return the orders
     */
    public Set<TradeOrder> getOrders() {
        return orders;
    }

    /**
     * Sets orders.
     *
     * @param orders the orders
     */
    public void setOrders(Set<TradeOrder> orders) {
        this.orders = orders;
    }

    /**
     * Gets local date time.
     *
     * @return the local date time
     */
    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    /**
     * Sets local date time.
     *
     * @param localDateTime the local date time
     */
    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    /**
     * Find order by user pair trade order.
     *
     * @param a the a
     * @param b the b
     * @return the trade order
     */
// A trade should contain only one pair of such users
    public TradeOrder findOrderByUserPair(User a, User b) {
        for (TradeOrder tradeOrder : orders) {
            if (tradeOrder.ifUserPairMatchOrder(a, b)) return tradeOrder;
        }
        return null;
    }

    /**
     * Find orders containing user collection.
     *
     * @param user the user
     * @return the collection
     */
    public Collection<TradeOrder> findOrdersContainingUser(User user) {
        Collection<TradeOrder> tradeOrders = new HashSet<>();
        for (TradeOrder tradeOrder : orders) {
            if (tradeOrder.ifUserInOrder(user)) tradeOrders.add(tradeOrder);
        }
        return tradeOrders;
    }

    /**
     * If user pair in order boolean.
     *
     * @param a the a
     * @param b the b
     * @return the boolean
     */
    public boolean ifUserPairInOrder(User a, User b) {
        return findOrderByUserPair(a, b) != null;
    }

    /**
     * User order bundle iterator iterator.
     *
     * @return the iterator
     */
    public Iterator<UserOrderBundle> userOrderBundleIterator() {
        return new TradeUserOrderBundleIterator(this);
    }

    /**
     * Find involved users collection.
     *
     * @return the collection
     */
    public Collection<User> findInvolvedUsers() {
        Set<User> users = new HashSet<>();
        getOrders().forEach(tradeOrder -> {
            users.add(tradeOrder.getLeftUser());
            users.add(tradeOrder.getRightUser());
        });
        return users;
    }

}
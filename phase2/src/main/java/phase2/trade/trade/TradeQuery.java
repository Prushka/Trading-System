package phase2.trade.trade;

import phase2.trade.user.User;

import java.util.Collection;

/**
 * The Trade query.
 *
 * @author Dan Lyu
 */
public class TradeQuery {

    private final Trade trade;

    /**
     * Constructs a new Trade query.
     *
     * @param trade the trade
     */
    public TradeQuery(Trade trade) {
        this.trade = trade;
    }

    /**
     * Find user lend count int.
     *
     * @param user the user
     * @return the int
     */
    public int findUserLendCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findBundleByUser(user).getTradeItemHolder().getLendCount();
        }
        return count;
    }

    /**
     * Find user borrow count int.
     *
     * @param user the user
     * @return the int
     */
    public int findUserBorrowCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findCounterBundleByUser(user).getTradeItemHolder().getLendCount();
        }
        return count;
    }

    /**
     * Find user sell count int.
     *
     * @param user the user
     * @return the int
     */
    public int findUserSellCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findCounterBundleByUser(user).getTradeItemHolder().getSellCount();
        }
        return count;
    }

}
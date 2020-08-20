package phase2.trade.trade;

import phase2.trade.user.User;

import java.util.Collection;

public class TradeQuery {

    private final Trade trade;

    public TradeQuery(Trade trade) {
        this.trade = trade;
    }

    public int findUserLendCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findBundleByUser(user).getTradeItemHolder().getLendCount();
        }
        return count;
    }

    public int findUserBorrowCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findCounterBundleByUser(user).getTradeItemHolder().getLendCount();
        }
        return count;
    }

    public int findUserSellCount(User user) {
        Collection<TradeOrder> orders = trade.findOrdersContainingUser(user);
        int count = 0;
        for (TradeOrder order : orders) {
            count += order.findCounterBundleByUser(user).getTradeItemHolder().getSellCount();
        }
        return count;
    }

}
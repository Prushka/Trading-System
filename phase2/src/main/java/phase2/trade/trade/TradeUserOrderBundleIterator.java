package phase2.trade.trade;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TradeUserOrderBundleIterator implements Iterator<UserOrderBundle> {

    private final Trade trade;
    private int curr;

    // The TradeOrders are stored as a Set in Trade
    // This List is only intended to be used internally
    // Since TradeOrder's order doesn't matter, this shouldn't have impact on the use case

    private final List<UserOrderBundle> tradeOrderBundles = new ArrayList<>();

    public TradeUserOrderBundleIterator(Trade trade) {
        this.trade = trade;
        trade.getOrders().forEach(tradeOrder -> {
            tradeOrderBundles.add(tradeOrder.getLeftBundle());
            tradeOrderBundles.add(tradeOrder.getRightBundle());
        });
    }

    @Override
    public boolean hasNext() {
        return curr < tradeOrderBundles.size() - 1;
    }

    @Override
    public UserOrderBundle next() {
        curr += 1;
        if (hasNext()) {
            return tradeOrderBundles.get(curr);
        }
        return null;
    }
}

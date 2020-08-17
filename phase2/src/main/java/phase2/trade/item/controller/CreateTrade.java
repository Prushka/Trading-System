package phase2.trade.item.controller;

import phase2.trade.inventory.TradeItemHolder;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.util.Collection;
import java.util.Map;

public class CreateTrade {

    private final Collection<Item> allItemsInvolved;

    private final Trade trade = new Trade();

    public CreateTrade(Collection<Item> allItemsInvolved) {
        this.allItemsInvolved = allItemsInvolved;
    }

    public void createTwoWayUserOrderBundle(User initiator, User target, Collection<Item> itemsInitiatorWant, Collection<Item> itemsTargetWant) {
        // find out what initiator will provide to target and vice versa

        for (TradeOrder order : trade.getOrders()) {
            if (order.getTarget().getUser().getUid().equals(target.getUid()) && order.getInitiator().getUser().getUid().equals(initiator.getUid())) {
                return;
            }
        }
        UserOrderBundle initiatorBundle = new UserOrderBundle();
        initiatorBundle.setUser(initiator);
        UserOrderBundle targetBundle = new UserOrderBundle();
        targetBundle.setUser(target);
        TradeItemHolder initiatorHolder = new TradeItemHolder();
        TradeItemHolder targetHolder = new TradeItemHolder();

        initiatorBundle.setTradeItemHolder(initiatorHolder);
        targetBundle.setTradeItemHolder(targetHolder);

        for (Item item : allItemsInvolved) {
            if (item.getOwner().getUid().equals(initiator.getUid()) && itemsTargetWant.contains(item)) { // initiator owns the item and target wants it
                initiatorHolder.addItem(item);
            } else if (item.getOwner().getUid().equals(target.getUid()) && itemsInitiatorWant.contains(item)) {
                targetHolder.addItem(item);
            }
        }
        TradeOrder order = new TradeOrder();
        order.setTarget(targetBundle);
        order.setInitiator(initiatorBundle);
        trade.getOrders().add(order);
        System.out.println(trade.getOrders().size());
    }

    public void createOrder(Map<User, Collection<Item>> userToItemToGet) {
        for (Map.Entry<User, Collection<Item>> entry : userToItemToGet.entrySet()) {
            for (Map.Entry<User, Collection<Item>> entry2 : userToItemToGet.entrySet()) {
                createTwoWayUserOrderBundle(entry.getKey(), entry2.getKey(), entry.getValue(), entry2.getValue());
                // only need combinations here. But whatever
            }
        }
    }

    public Trade getTrade() {
        return trade;
    }
}

package phase2.trade.trade.use;

import phase2.trade.item.Item;
import phase2.trade.itemlist.TradeItemHolder;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreateTrade {

    private final Map<User, Collection<Item>> usersToItemsToGet;

    private final Trade trade = new Trade();

    private final Collection<Item> allItems = new HashSet<>();

    private Set<User> usersInvolved = new HashSet<>();

    public CreateTrade(Map<User, Collection<Item>> usersToItemsToGet) {
        this.usersToItemsToGet = usersToItemsToGet;
        usersToItemsToGet.values().forEach(allItems::addAll);
    }


    public void createTwoWayUserOrderBundle(User initiator, User target, Collection<Item> itemsInitiatorWant, Collection<Item> itemsTargetWant) {
        // find out what initiator will provide to target and vice versa
        if (initiator == target) return;
        if (trade.ifUserPairInOrder(initiator, target)) return;
        UserOrderBundle initiatorBundle = new UserOrderBundle();
        initiatorBundle.setUser(initiator);
        UserOrderBundle targetBundle = new UserOrderBundle();
        targetBundle.setUser(target);
        TradeItemHolder initiatorHolder = new TradeItemHolder();
        TradeItemHolder targetHolder = new TradeItemHolder();

        initiatorBundle.setTradeItemHolder(initiatorHolder);
        targetBundle.setTradeItemHolder(targetHolder);

        for (Item item : allItems) {
            if (item.getOwner().getUid().equals(initiator.getUid()) && itemsTargetWant.contains(item)) { // initiator owns the item and target wants it
                initiatorHolder.addItem(item);
            } else if (item.getOwner().getUid().equals(target.getUid()) && itemsInitiatorWant.contains(item)) {
                targetHolder.addItem(item);
            }
        }
        TradeOrder order = new TradeOrder();
        order.setRightBundle(targetBundle);
        order.setLeftBundle(initiatorBundle);
        trade.getOrders().add(order);
    }

    // this cleanup doesn't remove order because even if two users do not have transaction, there is a chance user select both of them in the controller since
    // the number of users involved in arbitrary
    public void cleanup() {
        System.out.println(trade.getOrders().size());
        for (TradeOrder order : trade.getOrders()) {
            if (order.getRightBundle().getTradeItemHolder().size() == 0 && order.getLeftBundle().getTradeItemHolder().size() == 0) {
            } else {
                usersInvolved.add(order.getRightUser());
                usersInvolved.add(order.getLeftUser());
            }
        }
        // trade.getOrders().removeAll(ordersToRemove);

        System.out.println("Final Order Size: " + trade.getOrders().size());
    }

    public void createOrder() {
        for (Map.Entry<User, Collection<Item>> entry : usersToItemsToGet.entrySet()) {
            for (Map.Entry<User, Collection<Item>> entry2 : usersToItemsToGet.entrySet()) {
                createTwoWayUserOrderBundle(entry.getKey(), entry2.getKey(), entry.getValue(), entry2.getValue());
                // only need combinations here. But whatever
            }
        }
        cleanup();
    }

    public Trade getTrade() {
        return trade;
    }

    public Set<User> getUsersInvolved() {
        return usersInvolved;
    }
}
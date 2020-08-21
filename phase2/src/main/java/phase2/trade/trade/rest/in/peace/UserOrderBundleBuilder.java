package phase2.trade.trade.rest.in.peace;

import phase2.trade.item.Item;
import phase2.trade.itemlist.TradeItemHolder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import java.util.Set;

public class UserOrderBundleBuilder {
    User user;
    TradeItemHolder desiredItems = new TradeItemHolder();
    UserOrderBundle userRequests;

    public UserOrderBundleBuilder() {
    }

    void buildUser(User user) {
        this.user = user;
    }

    void buildDesiredItem(Item item) {
        desiredItems.addItem(item);
    }

    void buildDesiredItems(Set<Item> items) {
        desiredItems.setSetOfItems(items);
    }

    UserOrderBundle buildUserOrderBundle() {
        userRequests = new UserOrderBundle();
        userRequests.setUser(user);
        userRequests.setTradeItemHolder(desiredItems);
        return userRequests;
    }
}

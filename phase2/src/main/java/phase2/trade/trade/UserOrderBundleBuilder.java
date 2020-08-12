package phase2.trade.trade;

import java.util.*;

import phase2.trade.inventory.TradeItemHolder;
import phase2.trade.item.Item;
import phase2.trade.user.User;

public class UserOrderBundleBuilder {
    User user;
    TradeItemHolder desiredItems = new TradeItemHolder();
    UserOrderBundle userRequests;

    public UserOrderBundleBuilder(){}

    void buildUser(User user){ this.user = user; }

    void buildDesiredItem(Item item){
        desiredItems.addItem(item);
    }

    void buildDesiredItems(Set<Item> items){
        desiredItems.setSetOfItems(items);
    }

    UserOrderBundle buildUserOrderBundle() {
        userRequests = new UserOrderBundle();
        userRequests.setUser(user);
        userRequests.setTradeItemHolder(desiredItems);
        return userRequests;
    }
}

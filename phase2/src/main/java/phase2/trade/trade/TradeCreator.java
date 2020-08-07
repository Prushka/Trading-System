package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

public class TradeCreator {
    TradeBuilder tb;

    public TradeCreator(){}

    Trade createTrade(List<User> users, List<List<Item>> items, String year, String month, String day, String hour,
                      String minute, String country, String city, String street, String streetNum, boolean isPermanent){
        tb = new TradeBuilder();
        for (int i = 0; i < users.size(); i++){
            tb.buildUserOrderBundle(users.get(i), items.get(i));
        }
        tb.buildDateTime(year, month, day, hour, minute);
        tb.buildLocation(country, city, street, streetNum);
        tb.buildIsPermanent(isPermanent);
        return tb.buildTrade();
    }
}

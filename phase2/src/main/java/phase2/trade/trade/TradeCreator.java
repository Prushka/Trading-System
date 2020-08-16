package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class TradeCreator {
    TradeBuilder tb;

    public TradeCreator(){}

    public Trade createTrade(List<User> users, List<Set<Item>> items, String year, String month, String day, String hour,
                             String minute, String country, String city, String street, String streetNum, String isPermanent){
        tb = new TradeBuilder();
        for (int i = 0; i < users.size(); i++){
            tb.buildUserOrderBundle(users.get(i), items.get(i));
        }
        tb.buildDateTime(year, month, day, hour, minute);
        tb.buildLocation(country, city, street, streetNum);
        tb.buildIsPermanent(Boolean.parseBoolean(isPermanent));
        return tb.buildTrade();
    }
}

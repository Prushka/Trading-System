package phase2.trade.gateway;

import phase2.trade.item.Item;
import phase2.trade.user.User;

import java.util.List;

public interface ItemGateway extends Gateway<Item> {
    List<Item> findByName(String itemName);

    List<Item> findByCategory(String category);


}

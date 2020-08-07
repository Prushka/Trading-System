package phase2.trade.gateway.database;

import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;

public class ItemDAO extends DAO<Item> implements ItemGateway {


    public ItemDAO(DatabaseResourceBundle resource) {
        super(Item.class, resource);
    }


}

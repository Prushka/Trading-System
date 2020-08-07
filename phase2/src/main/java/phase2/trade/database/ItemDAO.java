package phase2.trade.database;

import phase2.trade.item.Item;

public class ItemDAO extends DAO<Item> implements ItemGateway {


    public ItemDAO(DatabaseResourceBundle resource) {
        super(Item.class, resource);
    }


}

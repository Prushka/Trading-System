package phase2.trade.database;

import phase2.trade.item.Item;

public class ItemDAO extends AbstractDAO<Item> {


    public ItemDAO(Class<Item> clazz, DatabaseResourceBundle resource) {
        super(clazz, resource);
    }


}

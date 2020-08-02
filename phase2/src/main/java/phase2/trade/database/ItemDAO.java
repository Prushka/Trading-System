package phase2.trade.database;

import org.hibernate.SessionFactory;
import phase2.trade.item.Item;

public class ItemDAO extends AbstractDAO<Item> {


    public ItemDAO(Class<Item> clazz, DatabaseResource resource) {
        super(clazz, resource);
    }
}

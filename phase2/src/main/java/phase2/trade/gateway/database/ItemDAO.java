package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.Gateway;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;

import java.util.List;

public class ItemDAO extends DAO<Item> implements ItemGateway {


    public ItemDAO(DatabaseResourceBundle resource) {
        super(Item.class, resource);
    }

    @Override
    public List<Item> findByName(String itemName) {
        Query query = getCurrentSession().createQuery("from Item where name = :itemName");
        query.setParameter("itemName", itemName);
        return query.list();
    }

    @Override
    public List<Item> findByCategory(String itemCategory) {
        Query query = getCurrentSession().createQuery("from Item where itemCategory = :category");
        query.setParameter("itemCategory", itemCategory);
        return query.list();
    }
}

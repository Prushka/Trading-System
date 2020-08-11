package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;

import java.util.List;

public class ItemDAO extends DAO<Item, ItemGateway> implements ItemGateway {


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
    public List<Item> findMarketItems() {
        Query query = getCurrentSession().createQuery("from Item where willingness != :ownerWillingness");
        query.setParameter("ownerWillingness", Willingness.NOPE);
        return query.list();
    }

    @Override
    protected ItemDAO getThis() {
        return this;
    }
}

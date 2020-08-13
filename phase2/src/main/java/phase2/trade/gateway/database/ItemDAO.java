package phase2.trade.gateway.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;
import phase2.trade.user.User;

import java.util.HashSet;
import java.util.List;

public class ItemDAO extends DAO<Item, ItemGateway> implements ItemGateway {


    public ItemDAO(DatabaseResourceBundle resource) {
        super(Item.class, resource);
    }

    @Override
    public List<Item> findByName(String itemName) {
        Query query = getCurrentSession().createQuery("FROM Item WHERE name = :itemName");
        query.setParameter("itemName", itemName);
        return query.list();
    }

    @Override
    public List<Item> findMarketItems() {
        Query query = getCurrentSession().createQuery("FROM Item WHERE willingness != :ownerWillingness");
        query.setParameter("ownerWillingness", Willingness.NOPE);
        return query.list();
    }

    @Override
    public List<Item> findPublicItemsFromOwner(Long ownerUID) {
        Query query = getCurrentSession().createQuery("FROM Item WHERE owner = :owner " +
                "AND ownership = :owned AND willingness IN :publicWillingness");
        query.setParameter("owner", ownerUID);
        query.setParameter("owned", Ownership.OWNER);
        query.setParameter("publicWillingness", new HashSet<Willingness>() {{
            add(Willingness.LEND);
            add(Willingness.SELL);
        }});
        return query.list();
    }

    @Override
    protected ItemDAO getThis() {
        return this;
    }
}

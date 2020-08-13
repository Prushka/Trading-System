package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

        CriteriaBuilder builder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Item> criteria = builder.createQuery(Item.class);
        Root<Item> root = criteria.from(Item.class);
        criteria.select(root);
        Query<Item> query = getCurrentSession().createQuery(criteria);
        List<Item> results = query.getResultList();

       //  Join<MyObject, JoinObject> joinObject = root.join("joinObject");

        // Query query = getCurrentSession().createQuery("FROM Item WHERE owner = :owner " +
        //         "AND ownership = :owned AND willingness IN :publicWillingness");
        // query.setParameter("owner", ownerUID);
        // query.setParameter("owned", Ownership.OWNER);
        // query.setParameter("publicWillingness", new HashSet<Willingness>() {{
        //     add(Willingness.LEND);
        //     add(Willingness.SELL);
        // }});
        return query.list();
    }

    @Override
    protected ItemDAO getThis() {
        return this;
    }
}

package phase2.trade.database;

import phase2.trade.gateway.ItemGateway;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends DAO<Item, ItemGateway> implements ItemGateway {


    public ItemDAO(DatabaseResourceBundle resource) {
        super(Item.class, resource);
    }

    @Override
    public List<Item> findMarketItems() {
        final List<Item> result = new ArrayList<>();

        criteria((builder, query, root) -> {
            Join<Item, User> user = root.join("owner");

            Predicate restriction = builder.and(
                    builder.notEqual(root.get("willingness"), Willingness.Private),
                    builder.notEqual(user.get("accountState"), AccountState.ON_VOCATION),
                    builder.equal(root.get("ownership"), Ownership.OWNER)
            );
            query.select(root).where(restriction);
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    public List<Item> findPublicItemsFromOwner(Long ownerUID) {
        final List<Item> result = new ArrayList<>();
        // criteria((builder, criteria, root) -> {
        //     // Predicate restriction = builder.and(
        //     //         builder..get("willingness"),Willingness.NOPE);
        //     criteria.select(root).where(restriction);
        //     executeCriteriaQuery(result, criteria);
        // });

        //  Join<MyObject, JoinObject> joinObject = root.join("joinObject");

        // Query query = getCurrentSession().createQuery("FROM Item WHERE owner = :owner " +
        //         "AND ownership = :owned AND willingness IN :publicWillingness");
        // query.setParameter("owner", ownerUID);
        // query.setParameter("owned", Ownership.OWNER);
        // query.setParameter("publicWillingness", new HashSet<Willingness>() {{
        //     add(Willingness.LEND);
        //     add(Willingness.SELL);
        // }});
        return result;
    }

    @Override
    protected ItemDAO getThis() {
        return this;
    }
}

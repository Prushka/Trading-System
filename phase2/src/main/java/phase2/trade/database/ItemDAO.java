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

/**
 * The {@link Item} data access object.
 *
 * @author Dan Lyu
 * @see Item
 */
public class ItemDAO extends DAO<Item, ItemGateway> implements ItemGateway {


    /**
     * Constructs a new Item dao.
     *
     * @param resource the resource
     */
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
                    builder.notEqual(user.get("accountState"), AccountState.ON_vacation),
                    builder.equal(root.get("ownership"), Ownership.OWNER)
            );
            query.select(root).where(restriction);
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    protected ItemDAO getThis() {
        return this;
    }
}

package phase2.trade.database;

import phase2.trade.gateway.TradeGateway;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class TradeDAO extends DAO<Trade, TradeGateway> implements TradeGateway {

    public TradeDAO(DatabaseResourceBundle resource) {
        super(Trade.class, resource);
    }

    @Override
    public List<Trade> findByUser(User currUser) {
        final List<Trade> result = new ArrayList<>();

        criteria((builder, query, root) -> {
            Join<Trade, TradeOrder> orders = root.join("orders");
            Join<TradeOrder, UserOrderBundle> leftBundles = orders.join("leftBundle");
            Join<TradeOrder, UserOrderBundle> rightBundles = orders.join("rightBundle");

            Predicate restriction = builder.or(
                    builder.equal(leftBundles.get("user"), currUser),
                    builder.equal(rightBundles.get("user"), currUser)
            );

            query.select(root).where(restriction);
            executeCriteriaQuery(result, query);
        });
        return result;

        /*
        // Citation needed? -- https://stackoverflow.com/questions/40461519/search-by-nested-property-of-collection-field-with-criteria-api
        CriteriaBuilder criteriaBuilder = getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<Trade> criteriaQuery = criteriaBuilder.createQuery(Trade.class);
        Root<Trade> root = criteriaQuery.from(Trade.class);

        Subquery<UserOrderBundle> userSubquery = criteriaQuery.subquery(UserOrderBundle.class);
        Root<Trade> tradeSubroot = userSubquery.correlate(root);
        Join<Trade, TradeOrder> tradeOrderJoin = tradeSubroot.join("myOrder");
        Join<TradeOrder, UserOrderBundle> bundleJoin = tradeOrderJoin.join("traders");
        userSubquery.select(bundleJoin);
        userSubquery.where(criteriaBuilder.equal(bundleJoin.get("user"), currUser));
        criteriaQuery.where(criteriaBuilder.exists(userSubquery));

        TypedQuery<Trade> tradeTypedQuery = getCurrentSession().createQuery(criteriaQuery);
        return tradeTypedQuery.getResultList();*/
    }

    // I'm not sure if these will work or if you can use function calls in query strings
    public int findNumOfTransactions(User currUser) {
        final List<Trade> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            Predicate restrictions = builder.or(
                    builder.and(
                            builder.equal(root.get("tradeState"), OrderState.CLOSED))
            );
            query.select(root).where(restrictions);
            executeCriteriaQuery(result, query);
        });
        return result.size();
    }

    public int findNumOfBorrowing(User currUser) {
        final List<Trade> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            Predicate restrictions = builder.or(
                    builder.and(
                            builder.equal(root.get("tradeState"), OrderState.CLOSED))
            );
            query.select(root).where(restrictions);
            executeCriteriaQuery(result, query);
        });
        return result.size();
    }

    public int findNumOfLending(User currUser) {
        final List<Trade> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            Predicate restrictions = builder.or(
                    builder.and(
                            builder.equal(root.get("tradeState"), OrderState.CLOSED))
            );
            query.select(root).where(restrictions);
            executeCriteriaQuery(result, query);
        });
        return result.size();
    }

    @Override
    protected TradeGateway getThis() {
        return this;
    }
}

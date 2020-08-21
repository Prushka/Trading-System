package phase2.trade.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeIdComparator;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The {@link Trade} data access object.
 *
 * @author Dan Lyu
 * @author Grace Leung
 * @see Trade
 */
public class TradeDAO extends DAO<Trade, TradeGateway> implements TradeGateway {

    private static final Logger logger = LogManager.getLogger(TradeDAO.class);

    /**
     * Constructs a new Trade dao.
     *
     * @param resource the resource
     */
    public TradeDAO(DatabaseResourceBundle resource) {
        super(Trade.class, resource);
    }


    @Override
    public Collection<Trade> findByUser(User currUser) {
        return findByUser(currUser, null, null);
    }

    @Override
    public Collection<Trade> findByUser(User currUser, LocalDateTime after, LocalDateTime before) {
        final Set<Trade> result = new HashSet<>();

        criteria((builder, query, root) -> {
            Join<Trade, TradeOrder> orders = root.join("orders");
            Join<TradeOrder, UserOrderBundle> leftBundles = orders.join("leftBundle");
            Join<TradeOrder, UserOrderBundle> rightBundles = orders.join("rightBundle");

            Predicate restriction = builder.or(
                    builder.equal(leftBundles.get("user"), currUser),
                    builder.equal(rightBundles.get("user"), currUser)
            );

            if (after != null && before != null) {
                restriction = builder.and(restriction,
                        builder.lessThan(root.get("localDateTime"), after),
                        builder.greaterThan(root.get("localDateTime"), before)
                );
            }

            query.select(root).where(restriction);
            executeCriteriaQuery(result, query);
        });
        logger.debug(result.size() + " trades find for user");
        List<Trade> newResult = new ArrayList<>(result);
        newResult.sort(new TradeIdComparator());
        return newResult;
    }

    public int findUserLendCount(User currUser) {
        final Set<UserOrderBundle> result = new HashSet<>();

        criteria(UserOrderBundle.class, (builder, query, root) -> {
            Join<Trade, TradeOrder> orders = root.join("orders");
            Join<TradeOrder, UserOrderBundle> leftBundles = orders.join("leftBundle");
            Join<TradeOrder, UserOrderBundle> rightBundles = orders.join("rightBundle");

            Predicate restrictionRight = builder.equal(rightBundles.get("user"), currUser);
            Predicate restrictionLeft = builder.equal(leftBundles.get("user"), currUser);

            query.select(leftBundles).where(restrictionLeft);
            query.select(rightBundles).where(restrictionRight);
            executeCriteriaQuery(result, query);
        });
        logger.debug(result.size() + " bundles find for user");

        final int[] borrowCount = {0};
        int sellCount = 0;

        result.forEach(userOrderBundle -> {
            borrowCount[0] += userOrderBundle.getTradeItemHolder().getLendCount();
        });
        return borrowCount[0];
    }


    @Override
    protected TradeGateway getThis() {
        return this;
    }
}

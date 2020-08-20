package phase2.trade.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.trade.*;
import phase2.trade.user.User;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;

public class TradeDAO extends DAO<Trade, TradeGateway> implements TradeGateway {

    private static final Logger logger = LogManager.getLogger(TradeDAO.class);

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

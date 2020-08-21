package phase2.trade.gateway;

import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * The {@link Trade} gateway interface.
 *
 * @author Dan Lyu
 * @author Grace Leung
 * @see Trade
 */
public interface TradeGateway extends EntityGateway<Trade, TradeGateway> {

    /**
     * Finds all trades of a given user.
     *
     * @param currUser the given user
     * @return a collection of trades
     */
    Collection<Trade> findByUser(User currUser);

    /**
     * Finds all trades of a given user within a time range.
     *
     * @param currUser the curr user
     * @param after    the after
     * @param before   the before
     * @return the collection
     */
    Collection<Trade> findByUser(User currUser, LocalDateTime after, LocalDateTime before);
}

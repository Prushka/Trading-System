package phase2.trade.gateway;

import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface TradeGateway extends EntityGateway<Trade, TradeGateway> {
    Collection<Trade> findByUser(User currUser);

    Collection<Trade> findByUser(User currUser, LocalDateTime after, LocalDateTime before);
}

package phase2.trade.gateway;

import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.util.List;

public interface TradeGateway extends EntityGateway<Trade, TradeGateway> {
    List<Trade> findByUser(User currUser);
}

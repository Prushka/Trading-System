package phase2.trade.gateway;

import phase2.trade.trade.Trade;

import java.util.List;

public interface TradeGateway extends EntityGateway<Trade, TradeGateway> {
    List<Trade> findByUser(Long id);
}

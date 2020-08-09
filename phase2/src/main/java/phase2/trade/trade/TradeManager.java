package phase2.trade.trade;

import phase2.trade.config.TradeConfig;
import phase2.trade.item.Item;
import phase2.trade.address.Address;
import phase2.trade.user.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
public class TradeManager {
    private TradeConfig tradeConfig;
    private TradeCreator tc;
    private TradeEditor te;
    private TradeConfirmer tcc;

    public TradeManager(TradeConfig tradeConfig){
        this.tradeConfig = tradeConfig;
        tc = new TradeCreator();
        te = new TradeEditor(tradeConfig.getEditLimit());
    }
}


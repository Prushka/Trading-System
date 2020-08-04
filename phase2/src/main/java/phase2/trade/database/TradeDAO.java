package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.util.List;

public class TradeDAO extends  DAO<Trade>{

    public TradeDAO(DatabaseResourceBundle resource) {
        super(Trade.class, resource);
    }
}

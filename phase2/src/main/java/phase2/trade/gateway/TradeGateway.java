package phase2.trade.gateway;

import phase2.trade.trade.Trade;
import phase2.trade.user.User;

import java.util.List;

public interface TradeGateway extends Gateway<Trade> {

    int findNumOfTransactions(User currUser);

    int findNumOfBorrowing(User currUser);

    int findNumOfLending(User currUser);
}

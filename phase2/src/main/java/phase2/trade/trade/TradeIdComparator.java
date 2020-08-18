package phase2.trade.trade;

import java.util.Comparator;

public class TradeIdComparator implements Comparator<Trade> {

    public int compare(Trade t1, Trade t2) {
        return Long.compare(t1.getUid(), t2.getUid());
    }

}
package group.trade;

import java.util.Iterator;

public class TradeFinderIterator {
    public Trade[] trades;
    // this part haven't done yet, needs to get all the trades

    public Iterator getIterator() {
        return new TradesIterator();
    }

    private class TradesIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            return index < trades.length;
        }

        @Override
        public Object next() {
            if(this.hasNext()){
                return trades[index++];
            }
            return null;
        }
    }
}


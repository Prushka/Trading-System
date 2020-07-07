import java.util.Iterator;

public class TradeFinderIterator {
    public Trade trades[];
    // this part haven't done yet, needs to get all the trades

    public Iterator getIterator() {
        return new TradesIterator();
    }

    private class TradesIterator implements Iterator {

        int index;

        @Override
        public boolean hasNext() {
            if(index < trades.length){
                return true;
            }
            return false;
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


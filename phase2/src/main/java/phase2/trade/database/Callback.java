package phase2.trade.database;

public interface Callback<T> {

    void call(T result);
}

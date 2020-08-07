package phase2.trade.gateway;

public interface Callback<T> {

    void call(T result);
}

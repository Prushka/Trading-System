package phase2.trade.callback;

@FunctionalInterface
public interface Callback<T> {

    void call(T result);
}

package phase2.trade.callback;

@FunctionalInterface
public interface StatusCallback<T> {

    void call(T result, ResultStatus resultStatus);
}

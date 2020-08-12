package phase2.trade.callback;

@FunctionalInterface
public interface ResultStatusCallback<T> {

    void call(T result, ResultStatus resultStatus);
}

package phase2.trade.callback;

import phase2.trade.callback.status.ResultStatus;

@FunctionalInterface
public interface ResultStatusCallback<T> {

    void call(T result, ResultStatus status);
}

package phase2.trade.callback;

import phase2.trade.callback.status.ResultStatus;

/**
 * The functional interface Result status callback.
 *
 * @param <T> the entity type to be called
 * @author Dan Lyu
 * @see Callback
 * @see StatusCallback
 * @see ResultStatus
 */
@FunctionalInterface
public interface ResultStatusCallback<T> {

    /**
     * Call.
     *
     * @param result the result
     * @param status the status
     */
    void call(T result, ResultStatus status);
}

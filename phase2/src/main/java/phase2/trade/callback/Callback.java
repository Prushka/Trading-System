package phase2.trade.callback;

/**
 * The interface Callback.
 *
 * @param <T> the entity type to be called
 * @author Dan Lyu
 * @see ResultStatusCallback
 * @see StatusCallback
 */
@FunctionalInterface
public interface Callback<T> {

    /**
     * Call.
     *
     * @param result the result
     */
    void call(T result);
}

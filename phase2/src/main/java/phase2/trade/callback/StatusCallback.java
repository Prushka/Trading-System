package phase2.trade.callback;

import phase2.trade.callback.status.ResultStatus;

/**
 * The interface Status callback.
 *
 * @author Dan Lyu
 * @see Callback
 * @see ResultStatusCallback
 * @see ResultStatus
 */
@FunctionalInterface
public interface StatusCallback {

    /**
     * Call.
     *
     * @param resultStatus the result status
     */
    void call(ResultStatus resultStatus);
}

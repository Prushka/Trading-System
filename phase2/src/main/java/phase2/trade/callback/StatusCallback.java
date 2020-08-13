package phase2.trade.callback;

import phase2.trade.callback.status.ResultStatus;

@FunctionalInterface
public interface StatusCallback {

    void call(ResultStatus resultStatus);
}

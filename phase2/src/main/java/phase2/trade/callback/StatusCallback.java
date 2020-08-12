package phase2.trade.callback;

@FunctionalInterface
public interface StatusCallback {

    void call(ResultStatus resultStatus);
}

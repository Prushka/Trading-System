package phase2.trade.exception;

public class IllegalUserOperationException
  extends RuntimeException {
    public IllegalUserOperationException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
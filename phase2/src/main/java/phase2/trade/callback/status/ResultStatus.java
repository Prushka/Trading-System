package phase2.trade.callback.status;

import javafx.application.Platform;
import phase2.trade.view.NotificationFactory;

public abstract class ResultStatus { // unsatisfied with the current implementation

    Runnable succeeded, failed, exist, after;

    protected String message = "";

    public ResultStatus(String message) {
        this();
        this.message = message;
    }

    public ResultStatus() {
        succeeded = () -> {};
        failed = () -> {};
        after = () -> {};
        exist = () -> {};
    }

    protected void toastMessage(NotificationFactory notificationFactory) {
        if (message != null && !message.isEmpty()) {
            notificationFactory.toast(message);
        }
    }

    public void setExist(Runnable exist) {
        this.exist = exist;
    }

    void run(Runnable runnable){
        Platform.runLater(runnable);
    }

    public abstract void handle(NotificationFactory notificationFactory);

    public void setSucceeded(Runnable succeeded) {
        this.succeeded = succeeded;
    }

    public void setFailed(Runnable failed) {
        this.failed = failed;
    }

    public void setAfter(Runnable after) {
        this.after = after;
    }

    public String getMessage() {
        return message;
    }

    public abstract boolean ifPass();
}

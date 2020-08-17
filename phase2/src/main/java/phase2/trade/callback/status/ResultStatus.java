package phase2.trade.callback.status;

import javafx.application.Platform;
import phase2.trade.view.PopupFactory;

public abstract class ResultStatus { // the whole thing may still need to be improved

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

    protected void toastMessage(PopupFactory popupFactory) {
        if (message != null && !message.isEmpty()) {
            popupFactory.toast(message);
        }
    }

    public void setExist(Runnable exist) {
        this.exist = exist;
    }

    void run(Runnable runnable){
        Platform.runLater(runnable);
    }

    public abstract void handle(PopupFactory popupFactory);

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

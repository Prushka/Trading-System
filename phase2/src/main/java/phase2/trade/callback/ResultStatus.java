package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public abstract class ResultStatus {

    StatusRunnable succeeded, failed, after;

    PopupFactory popupFactory;

    public ResultStatus() {
        succeeded = () -> {};
        failed = () -> {};
        after = () -> {};
    }

    void run(Runnable runnable){
        Platform.runLater(runnable);
    }

    public abstract void handle();

    public void setSucceeded(StatusRunnable succeeded) {
        this.succeeded = succeeded;
    }

    public void setFailed(StatusRunnable failed) {
        this.failed = failed;
    }

    public void setAfter(StatusRunnable after) {
        this.after = after;
    }

    public void setPopupFactory(PopupFactory popupFactory) {
        this.popupFactory = popupFactory;
    }

    public abstract boolean ifPass();
}

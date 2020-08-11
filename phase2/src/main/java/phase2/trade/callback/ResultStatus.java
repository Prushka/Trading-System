package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public abstract class ResultStatus {

    Runnable succeeded, failed, after;

    public ResultStatus() {
        succeeded = () -> {};
        failed = () -> {};
        after = () -> {};
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

    public abstract boolean ifPass();
}

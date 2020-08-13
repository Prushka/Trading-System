package phase2.trade.callback.status;

import javafx.application.Platform;
import phase2.trade.view.PopupFactory;

public abstract class ResultStatus { // the whole thing may still need to be improved

    Runnable succeeded, failed, exist, after;

    public ResultStatus() {
        succeeded = () -> {};
        failed = () -> {};
        after = () -> {};
        exist = () -> {};
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

    public abstract boolean ifPass();
}

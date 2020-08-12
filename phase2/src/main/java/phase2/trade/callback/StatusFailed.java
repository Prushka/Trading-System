package phase2.trade.callback;

import javafx.application.Platform;
import javafx.util.Duration;
import phase2.trade.presenter.PopupFactory;

public class StatusFailed extends ResultStatus {

    public StatusFailed() {

    }

    private String message;

    public StatusFailed(String message) {
        this.message = message;
    }

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            if (message != null) {
                popupFactory.toast(5, message, "CLOSE");
            }
            failed.run();
            after.run();
        });
    }

    @Override
    public boolean ifPass() {
        return false;
    }

}

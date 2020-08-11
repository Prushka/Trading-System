package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public class StatusSucceeded extends ResultStatus {

    @Override
    public void handle(PopupFactory popupFactory, Runnable succeed, Runnable failed) {
        Platform.runLater(succeed);
    }


    @Override
    public boolean ifPass() {
        return true;
    }
}

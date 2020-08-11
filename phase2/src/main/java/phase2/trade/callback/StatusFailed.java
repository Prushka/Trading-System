package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public class StatusFailed extends ResultStatus {

    @Override
    public void handle(PopupFactory popupFactory) {
            run(() -> {
                failed.run();
                after.run();
            });
    }

    @Override
    public boolean ifPass() {
        return false;
    }

}

package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.presenter.PopupFactory;

public class StatusExist extends StatusFailed {

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            failed.run();
            after.run();
        });
    }

}

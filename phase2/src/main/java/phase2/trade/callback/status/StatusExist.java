package phase2.trade.callback.status;

import phase2.trade.view.PopupFactory;

public class StatusExist extends StatusFailed {

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            failed.run();
            after.run();
        });
    }

}

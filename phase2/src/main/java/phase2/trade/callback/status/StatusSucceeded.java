package phase2.trade.callback.status;

import phase2.trade.view.PopupFactory;

public class StatusSucceeded extends ResultStatus {

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            succeeded.run();
            after.run();
        });
    }


    @Override
    public boolean ifPass() {
        return true;
    }
}

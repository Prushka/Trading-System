package phase2.trade.callback.status;

import phase2.trade.view.PopupFactory;

public class StatusSucceeded extends ResultStatus {

    public StatusSucceeded() {

    }

    public StatusSucceeded(String message) {
        super(message);
    }

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            toastMessage(popupFactory);
            succeeded.run();
            after.run();
        });
    }


    @Override
    public boolean ifPass() {
        return true;
    }
}

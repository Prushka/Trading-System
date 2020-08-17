package phase2.trade.callback.status;

import phase2.trade.view.PopupFactory;

public class StatusFailed extends ResultStatus {

    public StatusFailed() {
    }

    public StatusFailed(String message) {
        super(message);
    }

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            toastMessage(popupFactory);
            failed.run();
            after.run();
        });
    }

    @Override
    public boolean ifPass() {
        return false;
    }

}

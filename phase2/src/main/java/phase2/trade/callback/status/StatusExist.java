package phase2.trade.callback.status;

import phase2.trade.view.PopupFactory;

public class StatusExist extends StatusFailed {

    public StatusExist() {

    }

    public StatusExist(String message) {
        super(message);
    }

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            toastMessage(popupFactory);
            exist.run();
            failed.run();
            after.run();
        });
    }

}

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
            if (message != null) {
                popupFactory.toast(5, message, "CLOSE");
            }
            exist.run();
            failed.run();
            after.run();
        });
    }

}

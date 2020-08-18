package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

public class StatusExist extends StatusFailed {

    public StatusExist() {

    }

    public StatusExist(String message) {
        super(message);
    }

    @Override
    public void handle(NotificationFactory notificationFactory) {
        run(() -> {
            toastMessage(notificationFactory);
            exist.run();
            failed.run();
            after.run();
        });
    }

}

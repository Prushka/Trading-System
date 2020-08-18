package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

public class StatusSucceeded extends ResultStatus {

    public StatusSucceeded() {

    }

    public StatusSucceeded(String message) {
        super(message);
    }

    @Override
    public void handle(NotificationFactory notificationFactory) {
        run(() -> {
            toastMessage(notificationFactory);
            succeeded.run();
            after.run();
        });
    }


    @Override
    public boolean ifPass() {
        return true;
    }
}

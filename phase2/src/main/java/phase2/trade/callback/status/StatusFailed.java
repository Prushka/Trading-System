package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

public class StatusFailed extends ResultStatus {

    public StatusFailed() {
    }

    public StatusFailed(String message) {
        super(message);
    }

    @Override
    public void handle(NotificationFactory notificationFactory) {
        run(() -> {
            toastMessage(notificationFactory);
            failed.run();
            after.run();
        });
    }

    @Override
    public boolean ifPass() {
        return false;
    }

}

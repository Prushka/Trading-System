package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

/**
 * The failed Status.
 *
 * @author Dan Lyu
 */
public class StatusFailed extends ResultStatus {

    /**
     * Constructs a new Status failed.
     */
    public StatusFailed() {
    }

    /**
     * Constructs a new Status failed.
     *
     * @param message the message
     */
    public StatusFailed(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     * It toasts the message if exists.
     * Then the failed Runnable gets executed.
     * Finally, the after Runnable gets executed.
     */
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

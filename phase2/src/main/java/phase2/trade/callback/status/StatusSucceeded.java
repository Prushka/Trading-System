package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

/**
 * The succeeded status.
 *
 * @author Dan Lyu
 */
public class StatusSucceeded extends ResultStatus {

    /**
     * Constructs a new Status succeeded.
     */
    public StatusSucceeded() {

    }

    /**
     * Constructs a new Status succeeded.
     *
     * @param message the message
     */
    public StatusSucceeded(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     * It toasts the message if exists.
     * Then the succeeded Runnable gets executed.
     * Finally, the after Runnable gets executed.
     */
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

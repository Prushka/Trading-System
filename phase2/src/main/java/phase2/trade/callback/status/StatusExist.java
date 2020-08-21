package phase2.trade.callback.status;

import phase2.trade.view.NotificationFactory;

/**
 * The exist Status, used when something already exists.
 *
 * @author Dan Lyu
 */
public class StatusExist extends StatusFailed {

    /**
     * Constructs a new Status exist.
     */
    public StatusExist() {
    }

    /**
     * Constructs a new Status exist.
     *
     * @param message the message
     */
    public StatusExist(String message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     * It toasts the message if exists.
     * Then the exist Runnable gets executed.
     * Then the failed Runnable gets executed.
     * Finally, the after Runnable gets executed.
     */
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

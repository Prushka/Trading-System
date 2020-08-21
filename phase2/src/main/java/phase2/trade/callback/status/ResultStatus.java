package phase2.trade.callback.status;

import javafx.application.Platform;
import phase2.trade.view.NotificationFactory;

/**
 * The base Result status class. It contains 4 Runnable fields. Its subclasses will be responsible for running these Runnables.
 *
 * @author Dan Lyu
 */
public abstract class ResultStatus {

    /**
     * If the ResultStatus is a class that extends {@link StatusSucceeded}, this Runnable will be executed.
     */
    Runnable succeeded,
    /**
     * If the ResultStatus is a class that extends {@link StatusFailed}, this Runnable will be executed.
     */
    failed,
    /**
     * If the ResultStatus is a class that extends {@link StatusExist}, this Runnable will be executed.
     */
    exist,
    /**
     * This Runnable will always be executed after all other Runnables.
     */
    after;

    /**
     * The Language Identifier.
     */
    protected String message = "";

    /**
     * Constructs a new Result status.
     *
     * @param message the message
     */
    public ResultStatus(String message) {
        this();
        this.message = message;
    }

    /**
     * Constructs a new Result status.
     */
    public ResultStatus() {
        succeeded = () -> {
        };
        failed = () -> {
        };
        after = () -> {
        };
        exist = () -> {
        };
    }

    /**
     * Toast message using the notification factory.
     * A shortcut used to display any information using a NotificationFactory interface.
     * The NotificationFactory will be injected into ResultStatus by Controllers.
     *
     * @param notificationFactory the notification factory
     */
    protected void toastMessage(NotificationFactory notificationFactory) {
        if (message != null && !message.isEmpty()) {
            notificationFactory.toast(message);
        }
    }

    /**
     * Sets exist.
     *
     * @param exist the exist
     */
    public void setExist(Runnable exist) {
        this.exist = exist;
    }

    /**
     * Run using Platform.runLater. If the ResultStatus's Runnables are executed in an asynchronous thread, this prevents the UI from blocking the rest of the execution forever.
     * It's not possible to run anything related to view from another thread. The JavaFx view components can only be updated in JavaFX Application Thread.
     * Since the Runnables are in Controllers, it's fine to use Platform.runLater here in case the view gets updated in such Runnables.
     *
     * @param runnable the runnable
     */
    void run(Runnable runnable) {
        Platform.runLater(runnable);
    }

    /**
     * Handle.
     * The subclasses define the logic of running all Runnables.
     * This method should not be called from child class since it runs later.
     * Which mean the order of execution will not be kept if a <code>super.handle(notificationFactory)</code> is called from a child class.
     *
     * @param notificationFactory the notification factory
     */
    public abstract void handle(NotificationFactory notificationFactory);

    /**
     * Handle boolean. The result will be a boolean to determine if all execution passes.
     * To be used when there are multiple ResultStatuses and a final boolean result is required.
     *
     * @param notificationFactory the notification factory
     * @param ifPassed            the previous results
     * @return if the previous results && this all pass
     */
    public Boolean handle(NotificationFactory notificationFactory, Boolean ifPassed) {
        handle(notificationFactory);
        return ifPassed && ifPass();
    }

    /**
     * Sets succeeded.
     *
     * @param succeeded the succeeded
     */
    public void setSucceeded(Runnable succeeded) {
        this.succeeded = succeeded;
    }

    /**
     * Sets failed.
     *
     * @param failed the failed
     */
    public void setFailed(Runnable failed) {
        this.failed = failed;
    }

    /**
     * Sets after.
     *
     * @param after the after
     */
    public void setAfter(Runnable after) {
        this.after = after;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * If the ResultStatus indicates a Succeeded Status or a Failed status.
     *
     * @return <code>true</code> if succeeded
     */
    public abstract boolean ifPass();
}

package phase2.trade.callback.status;

import phase2.trade.permission.PermissionSet;
import phase2.trade.view.NotificationFactory;

/**
 * The no permission status, used when an operation is not permitted.
 *
 * @author Dan Lyu
 */
public class StatusNoPermission extends StatusFailed {

    private final PermissionSet permissionRequired;

    /**
     * Constructs a new Status no permission.
     *
     * @param permissionRequired the permission required
     */
    public StatusNoPermission(PermissionSet permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    /**
     * {@inheritDoc}
     * It toasts the Permission required.
     * Then the failed Runnable gets executed.
     * Finally, the after Runnable gets executed.
     */
    @Override
    public void handle(NotificationFactory notificationFactory) {
        run(() -> {
            notificationFactory.noPermission(permissionRequired);
            failed.run();
            after.run();
        });
    }

}

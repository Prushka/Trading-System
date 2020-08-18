package phase2.trade.callback.status;

import phase2.trade.permission.PermissionSet;
import phase2.trade.view.NotificationFactory;

public class StatusNoPermission extends StatusFailed {

    private PermissionSet permissionRequired;

    public StatusNoPermission(PermissionSet permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    @Override
    public void handle(NotificationFactory notificationFactory) {
        run(() -> {
            notificationFactory.noPermission(permissionRequired);
            failed.run();
            after.run();
        });
    }

}

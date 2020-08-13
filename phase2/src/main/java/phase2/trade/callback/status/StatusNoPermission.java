package phase2.trade.callback.status;

import phase2.trade.permission.PermissionSet;
import phase2.trade.view.PopupFactory;

public class StatusNoPermission extends StatusFailed {

    private PermissionSet permissionRequired;

    public StatusNoPermission(PermissionSet permissionRequired) {
        this.permissionRequired = permissionRequired;
    }

    @Override
    public void handle(PopupFactory popupFactory) {
        run(() -> {
            popupFactory.noPermission(permissionRequired);
            failed.run();
            after.run();
        });
    }

}

package phase2.trade.callback;

import javafx.application.Platform;
import phase2.trade.permission.PermissionSet;
import phase2.trade.presenter.PopupFactory;

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

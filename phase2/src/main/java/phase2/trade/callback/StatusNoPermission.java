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
    public void handle() {
        run(new Runnable() {
            @Override
            public void run() {

                popupFactory.noPermission();
                failed.run();
                after.run();
            }
        });
    }

}

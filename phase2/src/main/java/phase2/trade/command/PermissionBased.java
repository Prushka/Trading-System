package phase2.trade.command;

import phase2.trade.user.PermissionSet;

public interface PermissionBased {

    boolean checkPermission();

    PermissionSet getPermissionRequired();

}

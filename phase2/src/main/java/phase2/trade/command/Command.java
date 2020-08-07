package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

import java.util.HashSet;
import java.util.Set;

public abstract class Command<T> {

    User operator;

    Class<T> clazz;

    GatewayBundle gatewayBundle;

    Set<Long> effectedIds = new HashSet<>();

    public Command(GatewayBundle gatewayBundle, Class<T> clazz, User operator) {
        this.operator = operator;
        this.clazz = clazz;
        this.gatewayBundle = gatewayBundle;
    }

    public boolean checkPermission() {
        boolean all = true;
        for (Permission permissionRequired : getPermissionRequired().getPerm()) {
            all = all && operator.hasPermission(permissionRequired);
        }
        return all;
    }

    public Class<T> getClassToOperateOn() {
        return clazz;
    }

    public abstract PermissionSet getPermissionRequired();

    public abstract void execute(Callback<T> callback, String... args);

    public abstract void undo();

    public abstract void redo();

    private void save(){

    }

}
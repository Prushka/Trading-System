package phase2.trade.config.yaml;

import phase2.trade.user.Permission;

import java.util.List;

public class PermissionConfig {

    public List<Permission> ownerPermission;
    public List<Permission> adminPermission;
    public List<Permission> regularPermission;
    public List<Permission> guestPermission;

    public String getFileName() {
        return "permission_config";
    }
}

package phase2.trade.controller.side;

import phase2.trade.permission.PermissionGroup;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

public enum BottomSideOption {

    SIGN_OUT("side.sign.out", "/image/logout.png"),
    EXIT("side.exit", "/image/switch.png");
    

    public boolean ifDisplay(PermissionGroup permissionGroup) {
        return permissionGroups.contains(permissionGroup) || permissionGroups.size() == 0;
    }

    public String resourcePath;
    public String language;
    private Collection<PermissionGroup> permissionGroups = new HashSet<>();


    BottomSideOption(String language, String resourcePath, PermissionGroup... permissionGroups) {
        this.resourcePath = resourcePath;
        this.language = language;
        this.permissionGroups.addAll(Arrays.asList(permissionGroups));
    }

    BottomSideOption(String language, String resourcePath) {
        this.resourcePath = resourcePath;
        this.language = language;
    }
}

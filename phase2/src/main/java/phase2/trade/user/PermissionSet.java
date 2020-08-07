package phase2.trade.user;

import javax.persistence.*;
import java.util.*;

@Embeddable
public class PermissionSet {

    @ElementCollection(targetClass = Permission.class)
    private Set<Permission> perm = new HashSet<>();

    public PermissionSet() {
    }

    public PermissionSet(Set<Permission> perm) {
        this.perm = perm;
    }

    public PermissionSet(Permission... perms) {
        this();
        Collections.addAll(this.perm, perms);
    }

    public Set<Permission> getPerm() {
        return perm;
    }

    public boolean hasPermission(Permission permission) {
        return perm.contains(permission);
    }

    public void setPerm(Set<Permission> perm) {
        this.perm = perm;
    }

    public void removePerm(Permission perm) {
        this.perm.remove(perm);
    }
}

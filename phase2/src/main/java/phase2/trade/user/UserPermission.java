package phase2.trade.user;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Embeddable
public class UserPermission {

    @ElementCollection(targetClass=Permission.class)
    private Set<Permission> perm;

    public UserPermission() {
        this.perm = new HashSet<>();
    }

    public UserPermission(Set<Permission> perm) {
        this.perm = perm;
    }

    public Set<Permission> getPerm() {
        return perm;
    }

    public boolean hasPermission(Permission permission){
        return perm.contains(permission);
    }

    public void setPerm(Set<Permission> perm) {
        this.perm = perm;
    }

    public void removePerm(Permission perm){
        this.perm.remove(perm);
    }
}

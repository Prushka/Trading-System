package phase2.trade.user;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Embeddable
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ElementCollection(targetClass=Permission.class)
    private Set<Permission> perm;

    public UserPermission() {
        this.perm = new HashSet<>();
    }

    public UserPermission(Set<Permission> perm) {
        this.perm = perm;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

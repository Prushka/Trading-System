package phase2.trade.user;

import javax.persistence.*;
import java.util.Map;

//@Entity
@Embeddable
public class UserPermission {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long uid;

    // @Embedded
    // private Map<Permission, Boolean> perm;

//    public Long getUid() {
//        return uid;
//    }
//
//    public void setUid(Long uid) {
//        this.uid = uid;
//    }

    // public Map<Permission, Boolean> getPerm() {
    //     return perm;
    // }

    // public boolean hasPermission(Permission permission){
    //     if(!perm.containsKey(permission)) return false;
    //     return getPerm().get(permission);
    // }

    // public void setPerm(Map<Permission, Boolean> perm) {
    //     this.perm = perm;
    // }
}

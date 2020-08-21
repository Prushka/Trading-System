package phase2.trade.permission;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The Permission set.
 *
 * @author Dan Lyu
 */
@Embeddable
public class PermissionSet {

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Permission> perm = new HashSet<>();

    /**
     * Constructs a new Permission set.
     */
    public PermissionSet() {
    }

    /**
     * Constructs a new Permission set.
     *
     * @param perm the perm
     */
    public PermissionSet(Set<Permission> perm) {
        this.perm = perm;
    }

    /**
     * Constructs a new Permission set.
     *
     * @param perms the perms
     */
    public PermissionSet(Permission... perms) {
        this();
        Collections.addAll(this.perm, perms);
    }

    /**
     * Gets perm.
     *
     * @return the perm
     */
    public Set<Permission> getPerm() {
        return perm;
    }

    /**
     * Has permission boolean.
     *
     * @param permission the permission
     * @return the boolean
     */
    public boolean hasPermission(Permission permission) {
        return perm.contains(permission);
    }

    /**
     * Sets perm.
     *
     * @param perm the perm
     */
    public void setPerm(Set<Permission> perm) {
        this.perm = perm;
    }

    /**
     * Remove perm.
     *
     * @param perm the perm
     */
    public void removePerm(Permission perm) {
        this.perm.remove(perm);
    }

    @Override
    public String toString() {
        return getPerm().toString();
    }
}

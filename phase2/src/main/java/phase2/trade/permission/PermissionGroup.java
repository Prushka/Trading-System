package phase2.trade.permission;

/**
 * The enum Permission group.
 *
 * @author Dan Lyu
 * @author Lingyun Wang
 */
public enum PermissionGroup {

    // custom permission group?

    /**
     * System permission group.
     */
    SYSTEM,
    /**
     * Head admin permission group.
     */
    HEAD_ADMIN,
    /**
     * Admin permission group.
     */
    ADMIN,
    /**
     * Guest permission group.
     */
    GUEST,
    /**
     * Regular permission group.
     */
    REGULAR,
    /**
     * Frozen permission group.
     */
    FROZEN,
    /**
     * Banned permission group.
     */
    BANNED,

    /**
     * The Undefined.
     */
    UNDEFINED // do not use this one for any user, define your own
}

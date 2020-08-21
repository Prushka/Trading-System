package phase2.trade.command;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionGroup;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The runtime Command property annotation.<p>
 * The annotation will be processed using reflection in {@link Command}.
 *
 * @author Dan Lyu
 * @see Command
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @Inherited
@interface CommandProperty {

    /**
     * Crud type crud type.
     *
     * @return the crud type
     */
    CRUDType crudType();

    /**
     * Undoable boolean.
     *
     * @return the boolean
     */
    boolean undoable();

    /**
     * Persistent boolean.
     *
     * @return <code>true</code> if this Command's data will be persistent
     */
    boolean persistent();

    /**
     * Permission set.
     *
     * @return the permissions required
     */
    Permission[] permissionSet() default {}; // annotation doesn't allow PermissionSet, but allows array of Permissions

    /**
     * Permission group.
     *
     * @return the permission group required
     */
    PermissionGroup permissionGroup() default PermissionGroup.UNDEFINED;
}

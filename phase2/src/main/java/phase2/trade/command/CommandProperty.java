package phase2.trade.command;

import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @Inherited
@interface CommandProperty {

    CRUDType crudType();

    boolean isUndoable();

    boolean persistent();

    Permission[] permissionSet() default {}; // well annotation doesn't allow PermissionSet as an object

}

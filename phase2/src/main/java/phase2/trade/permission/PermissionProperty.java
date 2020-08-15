package phase2.trade.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public
@interface PermissionProperty {

    Permission[] permissionSet() default {};

}

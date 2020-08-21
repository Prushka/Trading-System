package phase2.trade.controller;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The runtime annotation Controller property.
 * The annotation will be processed using reflection in {@link ControllerFactory}.
 *
 * @author Dan Lyu
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @Inherited
@interface ControllerProperty {

    /**
     * View file path.<p>
     *
     * @return the view file path
     */
    String viewFile();

}

package phase2.trade.controller;

import phase2.trade.command.Command;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Documented
public @Inherited
@interface ControllerProperty {

    String viewFile();

    String[] involvedCommands() default {};

}

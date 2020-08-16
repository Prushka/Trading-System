package phase2.trade.command;

import java.util.List;

public interface ArgsInvolved {

    default String argRequired(Integer required, String... args) {
        return this.argRequired(required, null, args);
    }

    default String argRequired(Integer required, String defaultValue, String... args) {
        // required++;
        return args.length > required && args[required] != null ? args[required] : defaultValue;
    }

}
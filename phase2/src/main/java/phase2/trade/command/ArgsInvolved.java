package phase2.trade.command;

/**
 * This interface may become a standalone utility class or to be merged with {@link Command}.<p>
 * It's used to handle String args and get either an existing value or a default value.
 *
 * @author Dan Lyu
 */
public interface ArgsInvolved {

    /**
     * If the args have an element with the given index, the element will be returned.
     * Otherwise a null will be returned.
     *
     * @param required the required index
     * @param args     the args
     * @return the string element on that index or null
     */
    default String argRequired(Integer required, String... args) {
        return this.argRequired(required, null, args);
    }

    /**
     * If the args have an element with the given index, the element will be returned.
     * Otherwise the defaultValue will be returned.
     *
     * @param required     the required index
     * @param defaultValue the default value
     * @param args         the args
     * @return the string element on that index or the default value
     */
    default String argRequired(Integer required, String defaultValue, String... args) {
        // required++;
        return args.length > required && args[required] != null && !args[required].isEmpty() ? args[required] : defaultValue;
    }

}

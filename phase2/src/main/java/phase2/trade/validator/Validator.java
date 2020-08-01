package phase2.trade.validator;

/**
 * The functional interface used to validate an input String.<p>
 * Use this in lambda, anonymous inner class or implement a concrete class.<p>
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface Validator {

    /**
     * @param input String input
     * @return if the String input is valid
     */
    boolean validate(String input);

}

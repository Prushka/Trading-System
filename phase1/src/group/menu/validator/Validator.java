package group.menu.validator;

/**
 * The functional interface used to validate an input String.<p>
 * Use this in lambda, anonymous inner class or implement a concrete class.<p>
 * It is used to validate user input in {@link group.menu.node.InputNode}.
 *
 * @author Dan Lyu
 * @see group.menu.node.InputNode
 */

@FunctionalInterface
public interface Validator {

    /**
     * @param input String input
     * @return if the String input is valid
     */
    boolean validate(String input);

}

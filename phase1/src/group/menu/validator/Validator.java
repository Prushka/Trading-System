package group.menu.validator;

/**
 * Use this in lambda or implement a concrete class.<br>
 * It is used to validate user input in {@link group.menu.node.InputNode}.<br>
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface Validator {

    /**
     * @param input user input
     * @return if the user input is valid
     */
    boolean validate(String input);

}

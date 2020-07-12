package group.menu.processor;

/**
 * The functional interface to apply operations on a String
 * Use this in lambda or implement a concrete class.<p>
 * This will be used by {@link group.menu.node.InputNode} to apply certain format
 * on the use input String.
 *
 * @author Dan Lyu
 * @see group.menu.node.InputNode
 */

@FunctionalInterface
public interface InputPreProcessor {

    /**
     * @param input the raw input String
     * @return the processed input String
     */
    String process(String input);

}

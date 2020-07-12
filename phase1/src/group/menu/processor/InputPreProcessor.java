package group.menu.processor;

/**
 * Use this in lambda or implement a concrete class.<p>
 * This will be used by {@link group.menu.node.InputNode} to apply certain format
 * on the use input String.
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface InputPreProcessor {

    String process(String input);

}

package group.menu.data;

/**
 * Use this in lambda or implement a concrete class.<br>
 * This will be used by {@link group.menu.node.InputNode} to apply certain format
 * on the use input String.
 *
 * @author Dan Lyu
 */
public interface InputPreProcessor {

    String process(String input);

}

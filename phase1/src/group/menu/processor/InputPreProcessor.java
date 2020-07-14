package group.menu.processor;

import group.config.LoggerFactory;

import java.util.logging.Logger;

/**
 * The functional interface to apply operations on a String.<p>
 * Use this in lambda, anonymous inner class or implement a concrete class.<p>
 * This will be used by {@link group.menu.node.InputNode} to apply certain format
 * on the use input String.
 *
 * @author Dan Lyu
 * @see group.menu.node.InputNode
 */

@FunctionalInterface
public interface InputPreProcessor {

    Logger LOGGER = new LoggerFactory(InputPreProcessor.class).getConfiguredLogger();

    /**
     * @param input the raw input String
     * @return the processed input String
     */
    String process(String input);

}

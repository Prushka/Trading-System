package group.menu.handler;

import group.menu.data.Request;
import group.menu.data.Response;

/**
 * The functional interface to parse the Request to a Response in {@link group.menu.node.SubmitNode}.<p>
 * SubmitNode will use this when user submit successfully after the validation of user input.
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface RequestHandler {

    /**
     * Returns a Response object from a Request object after parsing it
     *
     * @param request the Request object to be parsed
     * @return the expected Response object
     */
    Response handle(Request request);

}

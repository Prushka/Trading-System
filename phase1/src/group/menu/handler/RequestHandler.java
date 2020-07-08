package group.menu.handler;

import group.menu.data.Request;
import group.menu.data.Response;

/**
 * Use this in lambda or implement a concrete class.<br>
 * It is used to parse the Request to a Response in {@link group.menu.node.SubmitNode}.<br>
 * SubmitNode will use this when user submit successfully after the validation of user input.
 *
 * @author Dan Lyu
 */

@FunctionalInterface
public interface RequestHandler {

    /**
     * @param request the Request object to be parsed
     * @return the expected Response object
     */
    Response handle(Request request);

}

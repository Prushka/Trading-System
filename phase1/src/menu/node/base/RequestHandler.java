package menu.node.base;

import menu.data.Request;
import menu.data.Response;

public interface RequestHandler {
    Response handle(Request request);
}

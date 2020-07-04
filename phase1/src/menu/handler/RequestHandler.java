package menu.handler;

import menu.data.Request;
import menu.data.Response;

public interface RequestHandler {

    Response handle(Request request);

}

package group.menu.handler;

import group.menu.data.Request;
import group.menu.data.Response;

public interface RequestHandler {

    Response handle(Request request);

}

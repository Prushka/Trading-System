package menu.node.base;

import menu.data.NodeRequest;
import menu.data.Response;

public interface RequestHandler {
    Response handle(NodeRequest request);
}

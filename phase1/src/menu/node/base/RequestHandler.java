package menu.node.base;

import menu.data.NodeRequest;

public interface RequestHandler {
    Node handle(NodeRequest request);
}

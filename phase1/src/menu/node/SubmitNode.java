package menu.node;

import menu.data.NodeRequest;
import menu.node.base.RequestHandler;
import menu.node.base.Node;
import menu.node.base.RequestableNode;
import menu.node.base.Validator;

import java.util.Optional;

public class SubmitNode extends InputNode {

    private RequestHandler handler;

    public SubmitNode(String translatable, String key, RequestHandler handler) {
        super(translatable, key);
        this.handler = handler;
    }

    public SubmitNode(String translatable, String key, ErrorNode errorNode, Validator validator, RequestHandler handler) {
        super(translatable, key, errorNode, validator);
        this.handler = handler;
    }

    public NodeRequest getRequest() { // construct request
        return constructRequest(this, new NodeRequest());
    }

    public NodeRequest constructRequest(RequestableNode curr, NodeRequest request) {
        if (curr.getParent() instanceof RequestableNode) {
            request.put(curr.getKey(), curr.getValue());
            curr = (RequestableNode) curr.getParent();
            constructRequest(curr, request);
        }
        return request;
    }

    @Override
    public Node parseInput(String input) {
        Optional<ErrorNode> error = validate();
        if (error.isPresent()) {
            return error.get();
        }
        return handler.handle(getRequest());
    }


}

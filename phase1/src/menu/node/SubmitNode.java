package menu.node;

import menu.data.NodeRequest;
import menu.data.Response;
import menu.node.base.RequestHandler;
import menu.node.base.Node;
import menu.node.base.RequestableNode;
import menu.node.base.Validator;

import java.util.Optional;

public class SubmitNode extends InputNode {

    private final RequestHandler handler;
    private ResponseNode resultNode;

    private Node failed;

    public SubmitNode(String translatable, String key, RequestHandler handler, Node failed) {
        this(translatable, key, null, null, handler, failed);
    }

    public SubmitNode(String translatable, String key, ResponseNode validateNode, Validator validator, RequestHandler handler, Node failed) {
        super(translatable, key, validateNode, validator);
        this.handler = handler;
        this.failed = failed;
    }

    public NodeRequest getRequest() {
        NodeRequest request = new NodeRequest();
        Node curr = this;
        while (curr instanceof RequestableNode) {
            request.put(((RequestableNode)curr).getKey(), ((RequestableNode)curr).getValue());
            curr = curr.getParent();
        }
        return request;
    }

    @Override
    public Node parseInput(String input) { // decouple
        this.value = input;
        Optional<ResponseNode> error = validate();
        if (error.isPresent()) {
            return error.get();
        }
        return parseResponse(handler.handle(getRequest()));
    }

    public Node parseResponse(Response response) {
        System.out.println(response);
        ResponseNode responseNode = new ResponseNode(response);
        //responseNode.setParent(this);
        if (response.getSuccess()) {
            responseNode.setChild(getChild());
        } else {
            responseNode.setChild(failed);
        }
        return responseNode;
    }


}

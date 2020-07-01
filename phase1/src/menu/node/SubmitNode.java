package menu.node;

import menu.data.NodeRequest;
import menu.node.base.Node;
import menu.node.base.RequestableNode;

import java.util.Optional;

public class SubmitNode extends InputNode {


    public SubmitNode(String translatable, String key) {
        super(translatable, key);
    }

    public NodeRequest submit() { // construct request
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
    public Node parseInput(String input){
        return null;
    }


}

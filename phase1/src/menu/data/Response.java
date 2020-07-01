package menu.data;

import menu.node.ErrorNode;
import menu.node.base.Node;

public class Response {

    private Node nextNode;

    private ErrorNode errorNode;

    public Response() {
    }

    public Node getNextNode() {
        return nextNode;
    }

    public ErrorNode getErrorNode() {
        return errorNode;
    }

    public Response nextNode(Node nextNode) {
        this.nextNode = nextNode;
        return this;
    }

    public Response errorNode(ErrorNode errorNode) {
        this.errorNode = errorNode;
        return this;
    }
}

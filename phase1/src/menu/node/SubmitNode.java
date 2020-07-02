package menu.node;

import menu.data.Request;
import menu.data.Response;
import menu.node.base.RequestHandler;

import java.util.Optional;

public class SubmitNode extends InputNode {

    private final RequestHandler handler;
    private final Node failedResultNode;

    public SubmitNode(Builder builder){
        super(builder);
        handler = builder.handler;
        failedResultNode = builder.failedResultNode;
    }

    public Request getRequest() {
        Request request = new Request();
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
        ResponseNode responseNode = new ResponseNode.Builder(response).build();
        //responseNode.setParent(this);
        if (response.getSuccess()) {
            responseNode.setChild(getChild());
        } else {
            responseNode.setChild(failedResultNode);
        }
        return responseNode;
    }

    public static class Builder extends AbstractInputNodeBuilder<Builder> {

        private RequestHandler handler;
        private Node failedResultNode;

        public Builder(String translatable) {
            super(translatable);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        public Builder handler(RequestHandler handler){
            this.handler = handler;
            return getThis();
        }

        public Builder submitSuccessNext(Node node){
            child(node);
            return getThis();
        }

        public Builder submitFailNext(ResponseNode node) {
            failedResultNode = node;
            return getThis();
        }

        @Override
        public SubmitNode build() {
            return new SubmitNode(this);
        }
    }
}

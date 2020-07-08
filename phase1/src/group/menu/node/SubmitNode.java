package group.menu.node;

import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.handler.RequestHandler;

import java.util.Optional;

public class SubmitNode extends InputNode {

    private final RequestHandler handler;

    private final Node failedResultNode;

    SubmitNode(Builder builder) {
        super(builder);
        handler = builder.handler;
        failedResultNode = builder.failedResultNode;
    }

    private Request getRequest() {
        Request request = new Request();
        Node curr = this;
        while (curr.acceptInput()) {
            request.put(curr.getKey(), curr.getValue());
            curr = curr.getParent();
        }
        request.setTimeStamp();
        return request;
    }

    @Override
    public Node parseInput(String input) {
        inputPreProcessing(input);
        Optional<Node> validateResult = validate();
        return validateResult.orElseGet(() -> parseResponse(handler.handle(getRequest())));
    }

    private Node parseResponse(Response response) {
        ResponseNode responseNode = new ResponseNode.Builder(response).build();
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

        public Builder(String translatable, String key, RequestHandler requestHandler) {
            super(translatable, key);
            handler = requestHandler;
        }

        @Override
        Builder getThis() {
            return this;
        }

        public Builder submitHandler(RequestHandler handler) {
            this.handler = handler;
            return getThis();
        }

        public Builder submitSuccessNext(Node node) {
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

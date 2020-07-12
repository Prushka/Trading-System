package group.menu.node;

import group.menu.data.PersistentRequest;
import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.handler.RequestHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SubmitNode extends InputNode {

    private final RequestHandler handler;

    private final Node failedResultNode;

    private final Map<String, MasterOptionNode> flexibleMasterPool;

    private final PersistentRequest persistentRequest;

    SubmitNode(Builder builder) {
        super(builder);
        handler = builder.handler;
        failedResultNode = builder.failedResultNode;
        flexibleMasterPool = builder.flexibleMasterPool;
        persistentRequest = builder.persistentRequest;
    }

    private Request getRequest() {
        Request request = new Request();
        Node curr = this;
        while (curr.acceptInput()) {
            request.put(curr.getKey(), curr.getValue());
            curr = curr.getParent();
        }
        request.setTimeStamp();
        request.setPersistentRequest(persistentRequest);
        return request;
    }

    public void fillMasterPool(MasterOptionNode master) {
        flexibleMasterPool.put(master.getTranslatable(), master);
    }

    @Override
    public Node parseInput(String input) {
        inputPreProcessing(input);
        Optional<Node> validateResult = validate();
        return validateResult.orElseGet(() -> parseResponse(handler.handle(getRequest())));
    }

    private Node parseResponse(Response response) {
        ResponseNode responseNode = new ResponseNode.Builder(response).build();
        if (response.getFlexibleMasterIdentifier() != null) {
            responseNode.setChild(flexibleMasterPool.get(response.getFlexibleMasterIdentifier()));
        } else if (response.success()) {
            responseNode.setChild(getChild());
            if (response.getPersistentKey() != null) {
                persistentRequest.addCachedRequest(response.getPersistentKey(), getRequest());
            }
        } else {
            responseNode.setChild(failedResultNode);
        }
        return responseNode;
    }

    public static class Builder extends AbstractInputNodeBuilder<Builder> {

        private RequestHandler handler;
        private Node failedResultNode;
        private final Map<String, MasterOptionNode> flexibleMasterPool = new HashMap<>();
        private final PersistentRequest persistentRequest;

        public Builder(String translatable, String key, RequestHandler handler, PersistentRequest persistentRequest) {
            super(translatable, key);
            this.handler = handler;
            this.persistentRequest = persistentRequest;
        }

        @Override
        Builder getThis() {
            return this;
        }

        public Builder master(MasterOptionNode node) {
            this.flexibleMasterPool.put(node.getTranslatable(), node);
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

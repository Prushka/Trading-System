package group.menu.node;

import group.menu.MasterOptionNodePool;
import group.menu.data.PersistentRequest;
import group.menu.data.Request;
import group.menu.data.Response;
import group.menu.handler.RequestHandler;

import java.util.Optional;

/**
 * A node that generates Request using parent Nodes, and will further generate a ResponseNode after parsing Request using an injected {@link RequestHandler}.<p>
 * This node accepts input and displays on its own.
 *
 * @author Dan Lyu
 * @see InputNode
 */
public class SubmitNode extends InputNode {

    /**
     * The injected handler used to parse Request and expect Response
     */
    private final RequestHandler handler;

    /**
     * The Node used when the Response returned by {@link #handler} doesn't represent a successful state
     */
    private final Node failedResultNode;


    private MasterOptionNodePool masterOptionNodePool;

    /**
     * The global persistent request object to be injected
     */
    private final PersistentRequest persistentRequest;

    /**
     * Constructs a SubmitNode from a {@link SubmitNode.Builder}
     *
     * @param builder the {@link SubmitNode.Builder}
     */
    SubmitNode(Builder builder) {
        super(builder);
        handler = builder.handler;
        failedResultNode = builder.failedResultNode;
        persistentRequest = builder.persistentRequest;
    }

    /**
     * A helper method used to grab keys and values from parent nodes who accept input into a Request object.
     *
     * @return the Request generated from previous nodes and current node
     */
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

    /**
     * Returns a Response node from {@link InputNode#validate()} if the validation fails.<p>
     * Returns a Response Node generated from the Response if validation succeeds.<p>
     *
     * @param input user input
     * @return the node to navigate to after parsing user input
     */
    @Override
    public Node parseInput(String input) {
        inputPreProcessing(input);
        Optional<Node> validateResult = validate();
        return validateResult.orElseGet(() -> parseResponse(handler.handle(getRequest())));
    }

    /**
     * Returns a Response Node generated from Response object
     * A helper method used to parse Response and generate Response Node that contains information.
     * If {@link Response#getNextMasterNodeIdentifier()} is not null, returns a Response Node who's child is {@link MasterOptionNode} that corresponds to the identifier.
     *
     * @param response the Response returned from the {@link #handler}
     * @return the node to navigate to after parsing user input
     */
    private Node parseResponse(Response response) {
        ResponseNode responseNode = new ResponseNode.Builder(response).build();
        Node realChild;
        if (response.getNextMasterNodeIdentifier() != null) {
            realChild = masterOptionNodePool.getMasterOptionNode(response.getNextMasterNodeIdentifier());
        } else if (response.success()) {
            realChild = getChild();
            if (response.getPersistentKey() != null) {
                persistentRequest.addCachedRequest(response.getPersistentKey(), getRequest());
            }
        } else {
            realChild = failedResultNode;
        }
        if (realChild == null) { // the next master node identifier doesn't exist in Response object, use the response success state and node from pool
            if (response.success()) {
                realChild = masterOptionNodePool.getSucceeded();
            } else {
                realChild = masterOptionNodePool.getFailed();
            }
        }
        responseNode.setChild(realChild);
        return responseNode;
    }

    /**
     * Add a MasterOptionNode into current SubmitNode in case this MasterOptionNode will be used by a Response object to navigate to.
     *
     * @param masterPool the {@link MasterOptionNodePool}
     */
    public void setMasterPool(MasterOptionNodePool masterPool) {
        masterOptionNodePool = masterPool;
    }

    /**
     * The builder used to build a SubmitNode.
     *
     * @author Dan Lyu
     */
    public static class Builder extends AbstractInputNodeBuilder<Builder> {

        /**
         * The injected handler used to parse Request and expect Response
         */
        private final RequestHandler handler;

        /**
         * The Node used when the Response returned by {@link #handler} doesn't represent a successful state
         */
        private Node failedResultNode;

        /**
         * The global persistent request object to be injected
         */
        private final PersistentRequest persistentRequest;

        public Builder(String translatable, String key, RequestHandler handler, PersistentRequest persistentRequest) {
            super(translatable, key);
            this.handler = handler;
            this.persistentRequest = persistentRequest;
        }

        /**
         * @return the builder itself
         */
        @Override
        Builder getThis() {
            return this;
        }

        /**
         * Works the same as setting the node as the child.
         *
         * @param node the node to navigate to when submission passes
         * @return the builder itself
         */
        public Builder submitSuccessNext(Node node) {
            child(node);
            return getThis();
        }

        /**
         * @param node the node to navigate to when submission fails
         * @return the builder itself
         */
        public Builder submitFailNext(ResponseNode node) {
            failedResultNode = node;
            return getThis();
        }

        /**
         * Builds a SubmitNode
         *
         * @return the built SubmitNode
         */
        @Override
        public SubmitNode build() {
            return new SubmitNode(this);
        }

    }

}

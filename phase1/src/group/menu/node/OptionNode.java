package group.menu.node;

import group.menu.data.Response;
import group.menu.handler.RequestHandler;

/**
 * The node that contains single option information.<p>
 * This node does not accept input and displays nothing on its own.<p>
 * This node will display option only when {@link MasterOptionNode} asks it to.
 *
 * @author Dan Lyu
 * @see MasterOptionNode
 */
public class OptionNode extends Node {

    /**
     * The id of the option
     */
    private final int id;

    private final RequestHandler handler;

    /**
     * Constructs an OptionNode from a {@link OptionNode.Builder}
     *
     * @param builder the {@link OptionNode.Builder}
     */
    OptionNode(Builder builder) {
        super(builder);
        this.id = builder.id;
        this.handler = builder.handler;
    }

    /**
     * @param translatable The translatable identifier
     * @param id           the id of the option
     */
    public OptionNode(String translatable, int id) {
        this(new Builder(translatable).id(id));
    }

    /**
     * Display nothing on its own.
     * The option node will be managed by {@link MasterOptionNode} to display information.
     */
    @Override
    public Response fetchResponse() {
        return new Response.Builder(true).translatable(getTranslatable(), getId()).build();
    }

    Response fetchProcessedResponse() {
        if (handler != null) {
            return new Response.Builder(true).response(handler.handle(null)).build();
        }
        return null;
    }

    /**
     * The Node doesn't accept input.
     *
     * @return false
     */
    @Override
    boolean acceptInput() {
        return false;
    }

    /**
     * @return the id of the option
     */
    public int getId() {
        return id;
    }

    /**
     * The Builder for {@link OptionNode}
     *
     * @author Dan Lyu
     */
    public static class Builder extends NodeBuilder<Builder> {

        /**
         * The option id
         */
        private int id;

        private RequestHandler handler;

        /**
         * Constructs a OptionNode.Builder
         *
         * @param translatable the translatable identifier
         */
        public Builder(String translatable) {
            super(translatable);
        }

        /**
         * @param id The option id
         * @return the Builder itself
         */
        public Builder id(int id) {
            this.id = id;
            return getThis();
        }

        public Builder requestHandler(RequestHandler handler) {
            this.handler = handler;
            return getThis();
        }

        /**
         * @return the OptionNode.Builder itself
         */
        @Override
        Builder getThis() {
            return this;
        }

        /**
         * Builds an OptionNode
         *
         * @return the OptionNode
         */
        @Override
        public OptionNode build() {
            return new OptionNode(this);
        }

    }

}

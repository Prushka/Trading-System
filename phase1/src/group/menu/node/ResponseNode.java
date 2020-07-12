package group.menu.node;

import group.menu.data.Response;
import group.menu.data.TranslatablePair;

import java.util.logging.Level;

/**
 * A node that contains only response information and does not accept input.
 *
 * @author Dan Lyu
 */
public class ResponseNode extends Node {

    /**
     * The response the ResponseNode will use to display information.
     */
    private final Response response;

    /**
     * Constructs a ResponseNode from a {@link ResponseNode.Builder}
     *
     * @param builder the {@link ResponseNode.Builder}
     */
    ResponseNode(Builder builder) {
        super(builder);
        this.response = builder.response;
    }

    /**
     * Constructs a ResponseNode from a translatable identifier
     *
     * @param translatable the translatable identifier
     */
    public ResponseNode(String translatable) {
        this(new ResponseNode.Builder(translatable));
    }

    /**
     * Constructs a ResponseNode from a Response object
     *
     * @param response the Response object
     */
    public ResponseNode(Response response) {
        this(new ResponseNode.Builder(response));
    }


    /**
     * Display the current response node.
     * All information in TranslatablePairs will be logged.
     */
    public void display() {
        if (response == null || response.getTranslatablePairs() == null || response.getTranslatablePairs().size() == 0) {
            LOGGER.log(Level.INFO, getTranslatable());
        } else {
            for (TranslatablePair pair : response.getTranslatablePairs()) {
                LOGGER.log(Level.INFO, pair.getTranslatable(), pair.getParas());
            }
        }
    }

    /**
     * @return false
     */
    @Override
    boolean acceptInput() {
        return false;
    }

    /**
     * The builder used to build a ResponseNode.
     *
     * @author Dan Lyu
     */
    public static class Builder extends NodeBuilder<Builder> {

        /**
         * The response the ResponseNode will use to display information.
         */
        private Response response;

        /**
         * @param translatable the translatable identifier
         */
        public Builder(String translatable) {
            super(translatable);
        }

        /**
         * @param response the response object
         */
        public Builder(Response response) {
            super("general.response.node");
            this.response = response;
        }

        /**
         * @param response the response object
         * @return the builder itself
         */
        public Builder response(Response response) {
            this.response = response;
            return getThis();
        }

        /**
         * @return the builder itself
         */
        @Override
        Builder getThis() {
            return this;
        }

        /**
         * Builds a ResponseNode
         *
         * @return the built ResponseNode
         */
        @Override
        public ResponseNode build() {
            return new ResponseNode(this);
        }

    }

}

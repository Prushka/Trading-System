package group.menu.node;


/**
 * The abstract class for nodes that accept input.
 *
 * @author Dan Lyu
 */
public abstract class RequestableNode extends Node {

    /**
     * The key to map user input
     */
    final String key;

    /**
     * The user input
     */
    String value;

    /**
     * Constructs a RequestableNode from a {@link RequestableNode.RequestableNodeBuilder}
     *
     * @param builder the {@link RequestableNode.RequestableNodeBuilder}
     */
    public RequestableNode(RequestableNodeBuilder<?> builder) {
        super(builder);
        this.key = builder.key;
    }

    /**
     * @return true
     */
    @Override
    boolean acceptInput() {
        return true;
    }

    /**
     * @return the key of the user input to be mapped to
     */
    public String getKey() {
        return key;
    }

    /**
     * @return the user input value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param <T> The type of a sub Node Builder that extends this current Node Builder
     * @author Dan Lyu
     */
    public abstract static class RequestableNodeBuilder<T extends RequestableNodeBuilder<T>> extends NodeBuilder<T> {

        /**
         * The key to map user input
         */
        private String key;

        /**
         * Constructs a Builder for {@link RequestableNode}
         *
         * @param translatable the translatable identifier
         * @param key          the key to map user input
         */
        public RequestableNodeBuilder(String translatable, String key) {
            super(translatable);
            this.key = key;
        }

        /**
         * @param key the key to map user input
         * @return the builder itself
         */
        public T key(String key) {
            this.key = key;
            return getThis();
        }

    }

}

package group.menu.node;


public abstract class RequestableNode extends Node {

    final String key;

    String value;

    public RequestableNode(RequestableNodeBuilder<?> builder) {
        super(builder);
        this.key = builder.key;
    }

    @Override
    boolean acceptInput() {
        return true;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public abstract static class RequestableNodeBuilder<T extends RequestableNodeBuilder<T>> extends NodeBuilder<T> {

        private String key;

        public RequestableNodeBuilder(String translatable) {
            super(translatable);
            this.key = translatable;
        }

        public RequestableNodeBuilder(String translatable, String key) {
            super(translatable);
            this.key = key;
        }

        public T key(String key) {
            this.key = key;
            return getThis();
        }

    }

}

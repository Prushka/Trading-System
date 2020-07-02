package menu.node;


import menu.node.base.Inputable;

public abstract class RequestableNode extends Node implements Inputable {
    protected final String key;
    protected String value;

    public RequestableNode(RequestableNodeBuilder<?> builder) {
        super(builder);
        this.key = builder.key;
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

        public T key(String key) {
            this.key = key;
            return getThis();
        }
    }
}

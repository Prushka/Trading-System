package group.menu.node;

import group.config.ConsoleLanguageFormatter;
import group.config.property.LanguageProperties;

import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public abstract class Node {

    static Logger LOGGER;

    static {
        LOGGER = Logger.getLogger(Node.class.getName());
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new ConsoleLanguageFormatter(new LanguageProperties()));
        LOGGER.addHandler(handler);
    }

    private Node child;

    private Node parent;

    private final String translatable;

    public Node(NodeBuilder<?> builder) {
        this.translatable = builder.translatable;
        setChild(builder.child);
    }

    public void display() {
        LOGGER.info(translatable);
    }

    abstract boolean acceptInput();

    public Node traceForward() {
        Node curr = this;
        while (curr != null && !curr.acceptInput()) {
            curr.display();
            curr = curr.getChild();
        }
        return curr;
    }

    public Node traceBack() {
        Node curr = this.getParent();
        while (curr != null && !curr.acceptInput()) {
            curr = curr.getParent();
        }
        if (curr == null) {
            return this;
        }
        return curr;
    }

    public Node getChild() {
        return child;
    }

    public Node setChild(Node node) {
        if (node != null) {
            this.child = node;
            node.parent = this;
        }
        return node;
    }

    public Node parseInput(String input) {
        return null;
    }

    void setParent(Node node) { // hmm
        this.parent = node;
    }

    public Node getParent() {
        return parent;
    }

    public String getTranslatable() {
        return translatable;
    }

    public String getIdentifier() {
        return getClass().getSimpleName() + " " + getTranslatable();
    }

    public String toString() {
        StringBuilder identifier = new StringBuilder("{");
        if (getParent() != null) {
            identifier.append("[").append("Parent: ").append(getParent().getIdentifier()).append("]");
        }
        identifier.append("[").append("Current: ").append(getIdentifier()).append("]");
        if (getChild() != null) {
            identifier.append("[").append("Child: ").append(getChild().getIdentifier()).append("]");
        }
        identifier.append("}");
        return identifier.toString();
    }

    abstract static class NodeBuilder<T extends NodeBuilder<T>> {

        private Node child;
        private final String translatable;

        public NodeBuilder(String translatable) {
            this.translatable = translatable;
        }

        public T child(Node child) {
            this.child = child;
            return getThis();
        }

        abstract T getThis();

        public T translatable(String translatable) {
            return getThis();
        }

        public abstract Node build();

    }

}

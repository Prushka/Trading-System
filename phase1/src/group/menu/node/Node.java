package group.menu.node;

import group.config.LoggerFactory;
import group.menu.data.Response;
import group.menu.persenter.ResponsePresenter;

import java.util.logging.Logger;

/**
 * The basic menu node that provides node related operations.<p>
 * Contains child, parent and has the ability to trace.
 *
 * @author Dan Lyu
 */
public abstract class Node {

    /**
     * The logger to log information from the Node
     */
    static final Logger LOGGER = new LoggerFactory(Node.class).getConfiguredLogger();

    /**
     * The child node
     */
    private Node child;

    /**
     * The parent node
     */
    private Node parent;

    /**
     * The translatable String
     */
    private final String translatable;

    /**
     * Constructs a Node from a {@link NodeBuilder}
     *
     * @param builder the {@link NodeBuilder}
     */
    public Node(NodeBuilder<?> builder) {
        this.translatable = builder.translatable;
        setChild(builder.child);
    }

    /**
     * Displays the current node's information.<p>
     * To be implemented by subclasses.<p>
     * To be sent to a {@link ResponsePresenter} to display.
     */
    public abstract Response fetchResponse();

    /**
     * @return <code>true</code> if the Node class accepts input
     */
    abstract boolean acceptInput();

    /**
     * @return The first child node that accepts input
     */
    public Node traceForward() {
        Node curr = this;
        while (curr != null && !curr.acceptInput()) {
            curr.fetchResponse();
            curr = curr.getChild();
        }
        return curr;
    }

    /**
     * @return The first parent node that accepts input
     */
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

    /**
     * @param input user input
     * @return the node to navigate to based on user input
     */
    public Node parseInput(String input) {
        return null;
    }

    /**
     * @return the child node of current node
     */
    public Node getChild() {
        return child;
    }

    /**
     * @param node the node to be the child of current node
     */
    public void setChild(Node node) {
        if (node != null) {
            this.child = node;
            node.parent = this;
        }
        // return node;
    }

    /**
     * @return the key to be used in Request
     */
    public String getKey() {
        return null;
    }

    /**
     * @return the user input value to be used in Request
     */
    public String getValue() {
        return null;
    }

    /**
     * @param node the parent node to be set
     */
    void setParent(Node node) { // hmm
        this.parent = node;
    }

    /**
     * @return the parent node of current node
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the translatable identifier
     */
    public String getTranslatable() {
        return translatable;
    }

    /**
     * The helper method to be used in {@link #toString()} to represent current node in String format.
     *
     * @return the identifier combination of current node
     */
    private String getIdentifier() {
        return getClass().getSimpleName() + " " + getTranslatable();
    }

    /**
     * @return the String representation of the current node, used only in debugging
     */
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

    /**
     * The Builder for a basic menu Node.
     *
     * @param <T> The type of a sub Node Builder that extends this current Node Builder
     * @author Dan Lyu
     */
    abstract static class NodeBuilder<T extends NodeBuilder<T>> {

        private Node child;

        private final String translatable;

        /**
         * @param translatable the translatable identifier for the current node
         */
        public NodeBuilder(String translatable) {
            this.translatable = translatable;
        }

        /**
         * @param child the child node of current node
         * @return builder itself
         */
        public T child(Node child) {
            this.child = child;
            return getThis();
        }

        /**
         * @return the most subtype of the builder
         */
        abstract T getThis();

        /**
         * Builds a node
         *
         * @return the built Node
         */
        public abstract Node build();

    }

}

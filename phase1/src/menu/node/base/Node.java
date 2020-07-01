package menu.node.base;

public abstract class Node {

    private Node child;

    public Node getChild() {
        return child;
    }

    private Node parent;

    private String translatable;

    public String getTranslatable() {
        return translatable;
    }

    public void display() {
        System.out.println(translatable);
    }

    public Node(String translatable) {
        this.translatable = translatable;
    }

    public Node setChild(Node node) {
        this.child = node;
        node.parent = this;
        return node;
    }

    public Node setParent(Node node) {
        this.parent = node;
        node.child = this;
        return node;
    }

    public Node getParent() {
        return parent;
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
}

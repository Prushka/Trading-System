package menu.node.base;

import config.ConsoleLanguageFormatter;
import config.property.LanguageProperties;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

public abstract class Node {

    protected static Logger LOGGER;

    private static void setLogger(LanguageProperties lang) {
        LOGGER = Logger.getLogger(Node.class.getName());
        LOGGER.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setFormatter(new ConsoleLanguageFormatter(lang));
        LOGGER.addHandler(handler);
    }

    private Node child;

    public Node getChild() {
        return child;
    }

    public Node setChild(Node node) {
        this.child = node;
        node.parent = this;
        return node;
    }

    private Node parent;

    public Node getParent() {
        return parent;
    }

    public Node setParent(Node node) {
        this.parent = node;
        node.child = this;
        return node;
    }

    private final String translatable;

    public String getTranslatable() {
        return translatable;
    }

    public void display() {
        LOGGER.info(translatable);
    }

    public Node(String translatable) {
        this.translatable = translatable;
        if (LOGGER == null) {
            setLogger(new LanguageProperties());
        }
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

package menu.node;

import menu.node.base.Node;
import menu.node.base.Skippable;

import java.util.logging.Level;

public class OptionNode extends Node implements Skippable {

    private final int id;

    public int getId() {
        return id;
    }

    public OptionNode(int id, String translatable) {
        super(translatable);
        this.id = id;
    }

    public void display() {

    }

    protected void displaySafe() {
        LOGGER.log(Level.INFO, getTranslatable(), getId());
    }

}

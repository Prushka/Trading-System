package menu.node;

import menu.node.base.Node;

public class OptionNode extends Node {

    private final int id;

    public int getId() {
        return id;
    }

    public OptionNode(int id, String translatable) {
        super(translatable);
        this.id = id;
    }

    public void display() {
        System.out.println("[" + id + "] " + getTranslatable());
    }

}

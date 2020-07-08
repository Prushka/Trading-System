package group.menu.node;

import java.util.Iterator;

public class OptionNodeIterator implements Iterator<Node> {

    private Node current;

    public OptionNodeIterator(OptionNode optionNode) {
        Node placeHolder = new ResponseNode("placeholder");
        placeHolder.setChild(optionNode);
        this.current = placeHolder;
    }

    @Override
    public boolean hasNext() {
        if (current == null || current.getChild() == null) return false;
        //while (!current.getChild().acceptInput()) {
        //    current = current.getChild(); // skip all nodes that are Response Nodes and Option Nodes
        //}
        return !(current.getChild() instanceof MasterOptionNode);// is there a way to get rid of this instance of?
    }

    @Override
    public Node next() {
        current = current.getChild();
        return current;
    }

}

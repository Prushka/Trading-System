package group.menu.node;

import java.util.Iterator;

/**
 * The Iterator used to iterate over the option node and its {@link InputNode} and {@link SubmitNode} children until it meets a {@link MasterOptionNode}.
 *
 * @author Dan Lyu
 */
public class OptionNodeIterator implements Iterator<Node> {

    /**
     * The current Node
     */
    private Node current;

    /**
     * @param optionNode The entry OptionNode
     */
    public OptionNodeIterator(OptionNode optionNode) {
        Node placeHolder = new ResponseNode("placeholder");
        placeHolder.setChild(optionNode);
        this.current = placeHolder;
    }

    /**
     * @return <code>true</code> if the current node has a node child which is not {@link MasterOptionNode}
     */
    @Override
    public boolean hasNext() {
        if (current == null || current.getChild() == null) return false;
        return !(current.getChild() instanceof MasterOptionNode);// is there a way to get rid of this instance of?
    }

    /**
     * @return the next node that matches the condition of {@link #hasNext()}
     */
    @Override
    public Node next() {
        current = current.getChild();
        return current;
    }

}

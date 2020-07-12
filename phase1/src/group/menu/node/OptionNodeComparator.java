package group.menu.node;

import java.util.Comparator;

/**
 * The class used to compare OptionNode's id
 *
 * @author Dan Lyu
 */
public class OptionNodeComparator implements Comparator<OptionNode> {

    /**
     * @param o1 The first OptionNode
     * @param o2 The second OptionNode
     * @return the int comparison result
     */
    @Override
    public int compare(OptionNode o1, OptionNode o2) {
        return o1.getId() - o2.getId();
    }

}

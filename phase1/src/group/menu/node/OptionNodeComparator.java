package group.menu.node;

import java.util.Comparator;

public class OptionNodeComparator implements Comparator<OptionNode> {

    @Override
    public int compare(OptionNode o1, OptionNode o2) {
        return o1.getId() - o2.getId();
    }

}

package group.menu;

import group.menu.data.Response;
import group.menu.node.MasterOptionNode;

import java.util.*;

public class MasterOptionNodePool {

    /**
     * The map of {@link MasterOptionNode} to find a corresponding Node to the {@link Response#getNextMasterNodeIdentifier()}
     * when a Response want the node to navigate to another {@link MasterOptionNode}. If a node is not specified and the SubmitNode has a
     * null child, the first element in the pool will be used.
     */
    private final Map<String, MasterOptionNode> availableNodes = new LinkedHashMap<>();

    private final Set<String> placeholders = new HashSet<>();

    private MasterOptionNode failed;

    private MasterOptionNode succeed;

    MasterOptionNodePool() {
    }

    public MasterOptionNode getFailed() {
        return failed;
    }

    public MasterOptionNode getSucceed() {
        return succeed;
    }

    MasterOptionNodePool setFailed(MasterOptionNode failed) {
        this.failed = failed;
        return this;
    }

    MasterOptionNodePool setSucceed(MasterOptionNode succeed) {
        this.succeed = succeed;
        return this;
    }

    MasterOptionNodePool addPlaceholder(String... nodes) {
        placeholders.addAll(Arrays.asList(nodes));
        return this;
    }

    public MasterOptionNode getMasterOptionNode(String key) {
        return availableNodes.get(key);
    }

    Set<String> getPlaceholders() {
        return placeholders;
    }

    void feedMe(Map<String, MasterOptionNode> masters) {
        for (String placeHolder : placeholders) {
            availableNodes.put(placeHolder, masters.get(placeHolder));
        }
    }

}

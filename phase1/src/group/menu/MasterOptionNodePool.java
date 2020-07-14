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

    private MasterOptionNode succeeded;

    private String succeededPlaceHolder;

    private String failedPlaceHolder;

    MasterOptionNodePool() {
    }

    void feedMe(Map<String, MasterOptionNode> masters) {
        for (String placeHolder : placeholders) {
            availableNodes.put(placeHolder, masters.get(placeHolder));
        }
        if (succeededPlaceHolder != null) {
            succeeded = masters.get(succeededPlaceHolder);
            availableNodes.put(succeededPlaceHolder, masters.get(succeededPlaceHolder));
        }
        if (failedPlaceHolder != null) {
            failed = masters.get(failedPlaceHolder);
            availableNodes.put(failedPlaceHolder, masters.get(failedPlaceHolder));
        }
    }

    public MasterOptionNode getFailed() {
        return failed;
    }

    public MasterOptionNode getSucceeded() {
        return succeeded;
    }

    void setSucceededPlaceHolder(String succeededPlaceHolder) {
        this.succeededPlaceHolder = succeededPlaceHolder;
    }

    void setFailedPlaceHolder(String failedPlaceHolder) {
        this.failedPlaceHolder = failedPlaceHolder;
    }

    void addPlaceholder(String... nodes) {
        placeholders.addAll(Arrays.asList(nodes));
    }

    public MasterOptionNode getMasterOptionNode(String key) {
        return availableNodes.get(key);
    }

}

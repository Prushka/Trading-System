package group.menu;

import group.menu.data.Response;
import group.menu.node.MasterOptionNode;

import java.util.*;

/**
 * A pool that holds multiple types of master option nodes to be used depending on the state of a Response object.
 *
 * @author Dan Lyu
 * @see Response
 * @see group.menu.node.SubmitNode
 */
public class MasterOptionNodePool {

    /**
     * The available MasterOptionNodes for the class that holds this Pool to use.<p>
     * When a Response want the node to navigate to another {@link MasterOptionNode}. If a node is not specified and the SubmitNode has a
     * null child, the first element in the pool will be used.
     */
    private final Map<String, MasterOptionNode> availableNodes = new LinkedHashMap<>();

    /**
     * Placeholders for MasterOptionNodes when building this pool.
     */
    private final Set<String> placeholders = new HashSet<>();

    /**
     * The MasterOptionNode to use when the Response failed
     */
    private MasterOptionNode failed;

    /**
     * The MasterOptionNode to use when the Response succeeded
     */
    private MasterOptionNode succeeded;

    /**
     * The placeholder for a succeeded MasterOptionNode
     */
    private String succeededPlaceHolder;

    /**
     * The placeholder for a failed MasterOptionNode
     */
    private String failedPlaceHolder;

    /**
     * Constructs a package protected MasterOptionNodePool
     */
    MasterOptionNodePool() {
    }

    /**
     * Replaces all placeholders in this class to the real Node object and put them into {@link #availableNodes}.
     *
     * @param masters a map of available MasterOptionNode
     */
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

    /**
     * @return The MasterOptionNode to use when the Response failed
     */
    public MasterOptionNode getFailed() {
        return failed;
    }

    /**
     * @return The MasterOptionNode to use when the Response succeeded
     */
    public MasterOptionNode getSucceeded() {
        return succeeded;
    }


    /**
     * @param succeededPlaceHolder The placeholder for a succeeded MasterOptionNode
     */
    void setSucceededPlaceHolder(String succeededPlaceHolder) {
        this.succeededPlaceHolder = succeededPlaceHolder;
    }

    /**
     * @param failedPlaceHolder The placeholder for a failed MasterOptionNode
     */
    void setFailedPlaceHolder(String failedPlaceHolder) {
        this.failedPlaceHolder = failedPlaceHolder;
    }

    /**
     * @param nodes the nodes' placeholders to be added into the placeholders set
     */
    void addPlaceholder(String... nodes) {
        placeholders.addAll(Arrays.asList(nodes));
    }

    /**
     * @param key the translatable of the MasterOptionNode
     * @return the MasterOptionNode object
     */
    public MasterOptionNode getMasterOptionNode(String key) {
        return availableNodes.get(key);
    }

}

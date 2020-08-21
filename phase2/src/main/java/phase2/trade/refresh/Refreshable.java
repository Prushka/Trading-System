package phase2.trade.refresh;

/**
 * The interface Refreshable.
 *
 * @author Dan Lyu
 * @see ReReReRe
 */
public interface Refreshable {

    /**
     * If the view has to be refreshed (Observable / data binding doesn't apply) manually, use the refresh(). No gateway involved
     */
    void refresh();
}

package phase2.trade.repository;

/**
 * The interface to define the entity to have a unique id.
 *
 * @author Dan Lyu
 */
public interface UniqueId {

    /**
     * @param value the unique id
     */
    void setUid(int value);

    /**
     * @return the unique id
     */
    int getUid();

}

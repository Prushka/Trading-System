package group.repository;

/**
 * The interface to define the entity to have a unique id.
 *
 * @author Dan Lyu
 */
public interface UniqueId {

    /**
     * @param value the unique id
     */
    void setUid(long value);

    /**
     * @return the unique id
     */
    long getUid();

}

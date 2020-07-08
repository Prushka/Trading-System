package group.repository;

/**
 * Implement this interface if you want to use the entity in Repository.
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

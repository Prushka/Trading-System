package phase2.trade.item;

/**
 * The enum Tag.
 *
 * @author Dan Lyu
 */
public enum Tag {

    /**
     * Used tag.
     */
    USED,
    /**
     * New tag.
     */
    NEW,
    /**
     * Stolen tag.
     */
    STOLEN,
    /**
     * Weed tag.
     */
    WEED;


    /**
     * The Resource path.
     */
    public String resourcePath;

    Tag(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    Tag() {

    }
}

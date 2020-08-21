package phase2.trade.config;

/**
 * The Ui config used to define sizes of certain nodes.
 *
 * @author Dan Lyu
 */
public class UIConfig {

    private int itemDescriptionPrefWidth = 250;

    private int itemNamePrefWidth = 100;

    private int permissionPrefWidth = 250;

    /**
     * Gets item description pref width.
     *
     * @return the item description pref width
     */
    public int getItemDescriptionPrefWidth() {
        return itemDescriptionPrefWidth;
    }

    /**
     * Sets item description pref width.
     *
     * @param itemDescriptionPrefWidth the item description pref width
     */
    public void setItemDescriptionPrefWidth(int itemDescriptionPrefWidth) {
        this.itemDescriptionPrefWidth = itemDescriptionPrefWidth;
    }

    /**
     * Gets permission pref width.
     *
     * @return the permission pref width
     */
    public int getPermissionPrefWidth() {
        return permissionPrefWidth;
    }

    /**
     * Sets permission pref width.
     *
     * @param permissionPrefWidth the permission pref width
     */
    public void setPermissionPrefWidth(int permissionPrefWidth) {
        this.permissionPrefWidth = permissionPrefWidth;
    }

    /**
     * Gets item name pref width.
     *
     * @return the item name pref width
     */
    public int getItemNamePrefWidth() {
        return itemNamePrefWidth;
    }

    /**
     * Sets item name pref width.
     *
     * @param itemNamePrefWidth the item name pref width
     */
    public void setItemNamePrefWidth(int itemNamePrefWidth) {
        this.itemNamePrefWidth = itemNamePrefWidth;
    }
}

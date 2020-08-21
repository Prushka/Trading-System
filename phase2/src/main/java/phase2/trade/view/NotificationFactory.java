package phase2.trade.view;

import javafx.util.Duration;
import phase2.trade.alert.*;
import phase2.trade.permission.PermissionSet;

/**
 * The Notification factory interface.
 *
 * @author Dan Lyu
 */
public interface NotificationFactory {
    /**
     * Toast.
     *
     * @param duration   the duration
     * @param message    the message
     * @param actionText the action text
     */
    void toast(Duration duration, String message, String actionText);

    /**
     * Toast.
     *
     * @param seconds    the seconds
     * @param message    the message
     * @param actionText the action text
     */
    void toast(int seconds, String message, String actionText);

    /**
     * Toast.
     *
     * @param seconds the seconds
     * @param message the message
     */
    void toast(int seconds, String message);

    /**
     * Toast.
     *
     * @param message the message
     */
    void toast(String message);

    /**
     * No permission.
     *
     * @param permissionRequired the permission required
     */
    void noPermission(PermissionSet permissionRequired);

    /**
     * No permission.
     */
    void noPermission();

    /**
     * Popup window popup alert.
     *
     * @param title  the title
     * @param header the header
     * @return the popup alert
     */
    PopupAlert popupWindow(String title, String header);

    /**
     * Confirm window confirm alert.
     *
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     * @param cancelButtonText  the cancel button text
     * @return the confirm alert
     */
    ConfirmAlert confirmWindow(String title, String header, String confirmButtonText, String cancelButtonText);

    /**
     * Table view alert table view alert.
     *
     * @param <T>    the type parameter
     * @param clazz  the clazz
     * @param title  the title
     * @param header the header
     * @return the table view alert
     */
    <T> TableViewAlert<T> tableViewAlert(Class<T> clazz, String title, String header);

    /**
     * V box alert v box alert.
     *
     * @param title  the title
     * @param header the header
     * @return the v box alert
     */
    VBoxAlert vBoxAlert(String title, String header);

    /**
     * V box alert v box alert.
     *
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     * @return the v box alert
     */
    VBoxAlert vBoxAlert(String title, String header, String confirmButtonText);

    /**
     * V box alert v box alert.
     *
     * @param title             the title
     * @param header            the header
     * @param confirmButtonText the confirm button text
     * @param cancelButtonText  the cancel button text
     * @return the v box alert
     */
    VBoxAlert vBoxAlert(String title, String header, String confirmButtonText, String cancelButtonText);

    /**
     * Split alert split alert.
     *
     * @param title  the title
     * @param header the header
     * @return the split alert
     */
    SplitAlert splitAlert(String title, String header);
}

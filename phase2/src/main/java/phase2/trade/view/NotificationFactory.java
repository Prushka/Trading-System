package phase2.trade.view;

import javafx.util.Duration;
import phase2.trade.alert.*;
import phase2.trade.permission.PermissionSet;

public interface NotificationFactory {
    void toast(Duration duration, String message, String actionText);

    void toast(int seconds, String message, String actionText);

    void toast(int seconds, String message);

    void toast(String message);

    void noPermission(PermissionSet permissionRequired);

    void noPermission();

    PopupAlert popupWindow(String title, String header);

    ConfirmAlert confirmWindow(String title, String header, String confirmButtonText, String cancelButtonText);

    <T> TableViewAlert<T> tableViewAlert(Class<T> clazz, String title, String header);

    VBoxAlert vBoxAlert(String title, String header);

    VBoxAlert vBoxAlert(String title, String header, String confirmButtonText);

    VBoxAlert vBoxAlert(String title, String header, String confirmButtonText, String cancelButtonText);

    SplitAlert splitAlert(String title, String header);
}

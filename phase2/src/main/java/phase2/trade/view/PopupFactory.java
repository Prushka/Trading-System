package phase2.trade.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import phase2.trade.alert.*;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;

public class PopupFactory implements NotificationFactory {

    protected Stage window;

    public PopupFactory(Stage root) {
        this.window = root;
    }

    @Override
    public void toast(Duration duration, String message, String actionText) {
        JFXSnackbar snackbar = new JFXSnackbar(getRootPane());
        snackbar.setPrefWidth(300);
        JFXButton button = new JFXButton(actionText);
        button.setOnAction(action -> snackbar.close());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message, actionText, action -> snackbar.close()),
                duration, null));
    }

    @Override
    public void toast(int seconds, String message, String actionText) {
        toast(Duration.seconds(seconds), message, actionText);
    }

    @Override
    public void toast(int seconds, String message) {
        toast(Duration.seconds(seconds), message, "CLOSE");
    }

    @Override
    public void toast(String message) {
        toast(Duration.seconds(5), message, "CLOSE");
    }

    @Override
    public void noPermission(PermissionSet permissionRequired) {
        StringBuilder perm = new StringBuilder();
        for (Permission permission : permissionRequired.getPerm()) {
            perm.append(permission).append(" ");
        }
        toast(Duration.seconds(4), "Permission Denied | Permission Required: " + perm, "CLOSE");
    }

    @Override
    public void noPermission() {
        toast(Duration.seconds(4), "Permission Denied", "CLOSE");
    }

    @Override
    public PopupAlert popupWindow(String title, String header) {
        return new PopupAlert(window, title, header);
    }

    @Override
    public ConfirmAlert confirmWindow(String title, String header, String confirmButtonText, String cancelButtonText) {
        return new ConfirmAlert(window, title, header, confirmButtonText, cancelButtonText);
    }

    @Override
    public <T> TableViewAlert<T> tableViewAlert(Class<T> clazz, String title, String header) {
        return new TableViewAlert<>(window, title, header, "Ok");
    }

    @Override
    public VBoxAlert vBoxAlert(String title, String header) {
        return vBoxAlert(title, header, "Ok");
    }

    @Override
    public VBoxAlert vBoxAlert(String title, String header, String confirmButtonText) {
        return vBoxAlert(title, header, confirmButtonText, "");
    }

    @Override
    public VBoxAlert vBoxAlert(String title, String header, String confirmButtonText, String cancelButtonText) {
        return new VBoxAlert(window, title, header, confirmButtonText, cancelButtonText);
    }

    @Override
    public SplitAlert splitAlert(String title, String header) {
        return new SplitAlert(window, title, header, "Ok", "Cancel");
    }

    protected Scene getRootScene() {
        return window.getScene();
    }

    protected Pane getRootPane() {
        return (Pane) getRootScene().getRoot();
    }
}

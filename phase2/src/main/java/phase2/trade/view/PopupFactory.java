package phase2.trade.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.view.window.ConfirmAlert;
import phase2.trade.view.window.PopupAlert;
import phase2.trade.view.window.TableViewAlert;
import phase2.trade.view.window.GeneralVBoxAlert;

public class PopupFactory {

    protected Stage window;

    public PopupFactory(Stage root) {
        this.window = root;
    }

    public void toast(Duration duration, String message, String actionText) {
        JFXSnackbar snackbar = new JFXSnackbar(getRootPane());
        snackbar.setPrefWidth(300);
        JFXButton button = new JFXButton(actionText);
        button.setOnAction(action -> snackbar.close());
        snackbar.fireEvent(new JFXSnackbar.SnackbarEvent(
                new JFXSnackbarLayout(message, actionText, action -> snackbar.close()),
                duration, null));
    }

    public void toast(int seconds, String message, String actionText) {
        toast(Duration.seconds(seconds), message, actionText);
    }

    public void toast(int seconds, String message) {
        toast(Duration.seconds(seconds), message, "CLOSE");
    }

    public void toast(String message) {
        toast(Duration.seconds(5), message, "CLOSE");
    }

    public void noPermission(PermissionSet permissionRequired) {
        StringBuilder perm = new StringBuilder();
        for (Permission permission : permissionRequired.getPerm()) {
            perm.append(permission).append(" ");
        }
        toast(Duration.seconds(4), "Permission Denied | " + perm, "CLOSE");
    }

    public void noPermission() {
        toast(Duration.seconds(4), "Permission Denied", "CLOSE");
    }

    public PopupAlert popupWindow(String title, String header) {
        return new PopupAlert(window, title, header);
    }

    public ConfirmAlert confirmWindow(String title, String header) {
        return new ConfirmAlert(window, title, header);
    }

    public <T> TableViewAlert<T> tableViewAlert(Class<T> clazz, String title, String header) {
        return new TableViewAlert<>(window, title, header);
    }

    public GeneralVBoxAlert textFieldAlert(String title, String header) {
        return new GeneralVBoxAlert(window, title, header);
    }

    protected Scene getRootScene() {
        return window.getScene();
    }

    protected Pane getRootPane() {
        return (Pane) getRootScene().getRoot();
    }
}

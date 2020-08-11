package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbarLayout;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.view.ConfirmWindow;
import phase2.trade.view.PopupWindow;

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

    public PopupWindow popupWindow(String title, String header) {
        return new PopupWindow(window, title, header);
    }

    public ConfirmWindow confirmWindow(String title, String header) {
        return new ConfirmWindow(window, title, header);
    }

    protected Scene getRootScene() {
        return window.getScene();
    }

    protected Pane getRootPane() {
        return (Pane) getRootScene().getRoot();
    }
}

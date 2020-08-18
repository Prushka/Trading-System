package phase2.trade.widget;

import javafx.scene.control.Label;
import phase2.trade.alert.AlertWindow;
import phase2.trade.controller.ControllerResources;
import phase2.trade.permission.Permission;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.TreeSet;

public class PermissionWidget extends WidgetControllerBase {

    private final Label title = new Label();
    private final Label group = new Label();
    private final Label permissionCounts = new Label();
    private final Label permissions = new Label();

    public PermissionWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    public void refresh() {
        title.setText("Permission");
        group.setText("Group: " + userToPresent.getPermissionGroup().toString());
        permissionCounts.setText("Permissions: " + userToPresent.getPermissionSet().getPerm().size());
        StringBuilder perms = new StringBuilder();
        Collection<Permission> permissionsToSort = new TreeSet<>(userToPresent.getPermissionSet().getPerm());
        permissionsToSort.forEach(perm -> perms.append(perm).append("\n"));
        permissions.setText(perms.toString());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-k");
        addTitle(title);
        addContent(group, permissionCounts);
        refresh();


        AlertWindow<?> alertWindow = getPopupFactory().vBoxAlert("Your Permissions: ", "");
        alertWindow.addNodes(permissions);

        setOnMouseClicked(e -> {
            alertWindow.display();
        });
    }
}

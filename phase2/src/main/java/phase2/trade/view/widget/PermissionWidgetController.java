package phase2.trade.view.widget;

import phase2.trade.controller.ControllerResources;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PermissionWidgetController extends WidgetControllerBase {

    private final Label title = new Label();
    private final Label group = new Label();
    private final Label permissionCounts = new Label();

    public PermissionWidgetController(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    protected void refresh() {
        title.setText("Permission");
        group.setText("Group - " + userToPresent.getPermissionGroup().toString());
        permissionCounts.setText("Permissions - " + userToPresent.getPermissionSet().getPerm().size());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-k");
    }
}

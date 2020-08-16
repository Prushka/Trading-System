package phase2.trade.view.widget;

import javafx.scene.control.Label;
import phase2.trade.controller.ControllerResources;

import java.net.URL;
import java.util.ResourceBundle;

public class UserWidget extends WidgetControllerBase {

    private final Label title = new Label();
    private final Label name = new Label();
    private final Label email = new Label();

    public UserWidget(ControllerResources controllerResources) {
        super(controllerResources);
    }

    @Override
    protected void refresh() {
        title.setText("User - " + userToPresent.getUid());
        name.setText("Name - " + userToPresent.getName());
        email.setText("Email - " + userToPresent.getEmail());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setGradient("gradient-j");
        addTitle(title);
        addContent(name,email);
        refresh();
    }
}

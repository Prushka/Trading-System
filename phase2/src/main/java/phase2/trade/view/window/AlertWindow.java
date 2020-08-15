package phase2.trade.view.window;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AlertWindow<T> extends CustomWindow<T> {

    private final String title, content;

    protected JFXAlert alert;

    protected JFXDialogLayout layout;

    protected VBox body;

    protected EventHandler<ActionEvent> confirmHandler;

    public AlertWindow(Stage parent, String title, String content) {
        super(parent);
        this.title = title;
        this.content = content;
        generateAlert();
    }

    public void clear() {
        body.getChildren().clear();
    }

    public void setEventHandler(EventHandler<ActionEvent> actionEventEventHandler) {
        this.confirmHandler = actionEventEventHandler;
    }

    public void addNodes(Node... nodes) {
        body.getChildren().addAll(nodes);
    }

    protected void generateAlert() {
        alert = new JFXAlert(parent);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        alert.setAnimation(JFXAlertAnimation.NO_ANIMATION);

        alert.setHideOnEscape(true);
        layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        body = new VBox(15);
        if (content != null && !content.isEmpty()) {
            body.getChildren().addAll(new Label(content));
        }
        layout.setBody(body);
    }

}

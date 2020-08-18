package phase2.trade.alert;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AlertWindow extends CustomWindow {

    private final String title, content;

    protected JFXAlert alert;

    protected JFXDialogLayout layout;

    protected VBox body;

    protected Button cancelButton;
    protected Button confirmButton;

    public AlertWindow(Stage parent, String title, String content) {
        this(parent, title, content, "", "");
    }

    public AlertWindow(Stage parent, String title, String content, String confirmButtonText) {
        this(parent, title, content, confirmButtonText, "");
    }

    public AlertWindow(Stage parent, String title, String content, String confirmButtonText, String cancelButtonText) {
        super(parent);
        this.title = title;
        this.content = content;

        confirmButton = getButton(confirmButtonText);
        cancelButton = getButton(cancelButtonText);
        generateAlert();
    }

    private Button getButton(String text) {
        Button button = new JFXButton(text);
        button.addEventFilter(ActionEvent.ACTION, event -> {
            alert.hideWithAnimation();
        });
        button.setFocusTraversable(false);
        button.setDefaultButton(true);
        return button;
    }

    public void clear() {
        body.getChildren().clear();
    }

    public void setConfirmHandler(EventHandler<ActionEvent> handler) {
        confirmButton.setOnAction(handler);
    }

    public void setCancelHandler(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
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

    public void display(String... args) {
        if (cancelButton.getText().isEmpty()) {
            layout.setActions(confirmButton);
        }
        alert.setContent(layout);
        alert.showAndWait();
    }

}

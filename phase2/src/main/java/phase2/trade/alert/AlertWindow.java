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

/**
 * The base class for all Alerts. Contains a text title, a text content, a confirm button and a cancel button.
 * This is again the wrapper of a JavaFx Alert.
 *
 * @author Dan Lyu
 */
public abstract class AlertWindow extends CustomWindow {

    private final String title, content;

    /**
     * The Alert itself.
     */
    protected JFXAlert alert;

    /**
     * The Dialog Layout.
     */
    protected JFXDialogLayout layout;

    /**
     * The VBox Body of this alert.
     */
    protected VBox body;

    /**
     * The Cancel button.
     */
    protected Button cancelButton;

    /**
     * The Confirm button.
     */
    protected Button confirmButton;

    /**
     * Constructs a new Alert window without any buttons.
     *
     * @param parent  the parent
     * @param title   the title
     * @param content the content
     */
    public AlertWindow(Stage parent, String title, String content) {
        this(parent, title, content, "", "");
    }

    /**
     * Constructs a new Alert window with a confirm button.
     *
     * @param parent            the parent
     * @param title             the title
     * @param content           the content
     * @param confirmButtonText the confirm button text
     */
    public AlertWindow(Stage parent, String title, String content, String confirmButtonText) {
        this(parent, title, content, confirmButtonText, "");
    }

    /**
     * Constructs a new Alert window with a confirm button and a cancel button.
     *
     * @param parent            the parent
     * @param title             the title
     * @param content           the content
     * @param confirmButtonText the confirm button text
     * @param cancelButtonText  the cancel button text
     */
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

    /**
     * Clear the body of this alert.
     */
    public void clear() {
        body.getChildren().clear();
    }

    /**
     * Sets event handler for the confirm button.
     *
     * @param handler the handler
     */
    public void setConfirmHandler(EventHandler<ActionEvent> handler) {
        confirmButton.setOnAction(handler);
    }

    /**
     * Sets event handler for the cancel button.
     *
     * @param handler the handler
     */
    public void setCancelHandler(EventHandler<ActionEvent> handler) {
        cancelButton.setOnAction(handler);
    }

    /**
     * Add view nodes to the body.
     *
     * @param nodes the nodes
     */
    public void addNodes(Node... nodes) {
        body.getChildren().addAll(nodes);
    }

    private void generateAlert() {
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

    public void display() {
        if (cancelButton.getText().isEmpty()) {
            layout.setActions(confirmButton);
        }
        alert.setContent(layout);
        alert.showAndWait();
    }

}

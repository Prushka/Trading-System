package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

// this is not the splitpane, but instead two VBoxes in an HBox
// the name may change in the future to avoid confusion
public class GeneralSplitAlert extends AlertWindow<Void> {

    private final HBox friend;

    private final VBox right;

    public GeneralSplitAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
        friend = new HBox(10);
        right = new VBox(35);
        friend.getChildren().addAll(body, right);
    }

    public void addLeft(Node... nodes) {
        addNodes(nodes);
    }

    public void addRight(Node... nodes) {
        right.getChildren().addAll(nodes);
    }

    public Void display(String... args) {
        Button confirmButton = new JFXButton("Ok");

        confirmButton.setOnAction(confirmHandler);
        confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
            alert.hideWithAnimation();
        });
        confirmButton.setFocusTraversable(false);

        Button cancelButton = new JFXButton("Cancel");

        cancelButton.setOnAction(confirmHandler);
        cancelButton.addEventFilter(ActionEvent.ACTION, event -> {
            alert.hideWithAnimation();
        });
        cancelButton.setFocusTraversable(false);

        body.setMinWidth(250);
        right.setMinWidth(250); // use a configuration file in the future

        alert.setOverlayClose(false);

        layout.getBody().setAll(friend);
        layout.setActions(cancelButton, confirmButton);

        alert.setContent(layout);

        alert.showAndWait();
        return null;
    }

}

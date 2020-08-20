package phase2.trade.alert;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// this is not the SplitPane, but instead two VBoxes in an HBox
// the name may change in the future to avoid confusion
public class SplitAlert extends AlertWindow {

    private final HBox root;

    private final VBox right;

    public SplitAlert(Stage parent, String title, String header, String confirmText, String cancelText) {
        super(parent, title, header, confirmText, cancelText);
        body.setSpacing(35);
        root = new HBox(10);
        right = new VBox(35);
        root.getChildren().addAll(body, right);
    }

    public void addLeft(Node... nodes) {
        addNodes(nodes);
    }

    public void addRight(Node... nodes) {
        right.getChildren().addAll(nodes);
    }

    public void display(String... args) {
        body.setMinWidth(250);
        right.setMinWidth(250); // use a configuration file in the future

        alert.setOverlayClose(false);

        layout.getBody().setAll(root);
        layout.setActions(cancelButton, confirmButton);

        super.display(args);
    }

}

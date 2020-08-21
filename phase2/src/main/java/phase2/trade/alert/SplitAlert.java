package phase2.trade.alert;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/**
 * The Split alert with an HBox root, a left VBox and a right VBox.
 * Not to be confused with SplitPane.
 *
 * @author Dan Lyu
 */
public class SplitAlert extends AlertWindow {

    private final HBox root;

    private final VBox right;

    /**
     * Constructs a new Split alert.
     *
     * @param parent      the parent
     * @param title       the title
     * @param header      the header
     * @param confirmText the confirm text
     * @param cancelText  the cancel text
     */
    public SplitAlert(Stage parent, String title, String header, String confirmText, String cancelText) {
        super(parent, title, header, confirmText, cancelText);
        body.setSpacing(35);
        root = new HBox(10);
        right = new VBox(35);
        root.getChildren().addAll(body, right);
    }

    /**
     * Add a view node to the left VBox.
     *
     * @param nodes the nodes
     */
    public void addLeft(Node... nodes) {
        addNodes(nodes);
    }

    /**
     * Add a view node to the right VBox.
     *
     * @param nodes the nodes
     */
    public void addRight(Node... nodes) {
        right.getChildren().addAll(nodes);
    }

    public void display() {
        body.setMinWidth(250);
        right.setMinWidth(250); // use a configuration file in the future

        alert.setOverlayClose(false);

        layout.getBody().setAll(root);
        layout.setActions(cancelButton, confirmButton);

        super.display();
    }

}

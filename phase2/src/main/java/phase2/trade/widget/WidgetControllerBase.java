package phase2.trade.widget;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.User;

/**
 * The Widget controller base.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "widget.fxml")
public abstract class WidgetControllerBase extends AbstractController implements Initializable {

    @FXML
    private VBox root;

    /**
     * The User to present.
     */
    protected final User userToPresent;

    /**
     * Constructs a new Widget controller base.
     *
     * @param controllerResources the controller resources
     */
    public WidgetControllerBase(ControllerResources controllerResources) {
        super(controllerResources);
        userToPresent = getAccountManager().getLoggedInUser();
    }

    /**
     * Add nodes.
     *
     * @param nodes the nodes
     */
    public void addNodes(Node... nodes) {
        root.getChildren().addAll(nodes);
    }

    /**
     * Clear.
     */
    public void clear() {
        root.getChildren().clear();
    }

    /**
     * Add title.
     *
     * @param nodes the nodes
     */
    public void addTitle(Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().addAll("widget-header");
        }
        root.getChildren().addAll(nodes);
    }

    /**
     * Add content.
     *
     * @param nodes the nodes
     */
    public void addContent(Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().addAll("widget-body");
        }
        root.getChildren().addAll(nodes);
    }

    /**
     * Sets on mouse clicked.
     *
     * @param value the value
     */
    public void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        root.setOnMouseClicked(value);
    }

    /**
     * Gets root.
     *
     * @return the root
     */
    public VBox getRoot() {
        return root;
    }

    /**
     * Sets gradient.
     *
     * @param gradient the gradient
     */
    protected void setGradient(String gradient) {
        this.root.getStyleClass().add(gradient);
    }

}

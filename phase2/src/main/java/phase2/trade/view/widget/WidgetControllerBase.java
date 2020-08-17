package phase2.trade.view.widget;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.EditableController;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.user.User;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "widget.fxml")
public abstract class WidgetControllerBase extends AbstractController implements Initializable {

    @FXML
    private VBox root;

    protected final User userToPresent;

    public WidgetControllerBase(ControllerResources controllerResources) {
        super(controllerResources);
        userToPresent = getAccountManager().getLoggedInUser();
    }

    public void addNodes(Node... nodes) {
        root.getChildren().addAll(nodes);
    }

    public void addTitle(Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().addAll("widget-header");
        }
        root.getChildren().addAll(nodes);
    }

    public void addContent(Node... nodes) {
        for (Node node : nodes) {
            node.getStyleClass().addAll("widget-body");
        }
        root.getChildren().addAll(nodes);
    }

    public void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        root.setOnMouseClicked(value);
    }

    public Pane getRoot() {
        return root;
    }

    protected void setGradient(String gradient) {
        this.root.getStyleClass().add(gradient);
    }

}

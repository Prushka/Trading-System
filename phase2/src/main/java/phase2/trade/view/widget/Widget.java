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
import phase2.trade.controller.ControllerProperty;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "abstract_v.fxml")
public class Widget {

    @FXML
    private final VBox root;

    private Map<String, Labeled> labels = new HashMap<>();

    public Widget(String gradient, int width, int height) {
        root = new VBox(10);
        root.setPrefWidth(width);
        root.setPrefHeight(height);
        root.setAlignment(Pos.CENTER_LEFT);
        root.setPadding(new Insets(15));
        //Button button = getNodeFactory().getDefaultFlatButton("", new Color(0.4, 0.4, 0.4, 0.2));

        root.getStyleClass().addAll("butter", gradient);
        //button.getStyleClass().addAll("butter", gradient);
        //button.setGraphic(vBox);
    }

    // the label will be retrievable in the future, no casting required
    public void addLabeled(String key, Labeled label) {
        root.getChildren().add(label);
        labels.put(key, label);
    }

    public void addNodes(Node... nodes) {
        root.getChildren().addAll(nodes);
    }

    public void addTitle(Labeled... nodes) {
        for (Labeled node : nodes) {
            node.getStyleClass().addAll("butter-header");
        }
        root.getChildren().addAll(nodes);
    }

    public void addContent(Labeled... nodes) {
        for (Labeled node : nodes) {
            node.getStyleClass().addAll("butter-body");
        }
        root.getChildren().addAll(nodes);
    }

    public ObservableList<Node> getChildren() {
        return root.getChildren();
    }

    public void setOnMouseClicked(EventHandler<? super MouseEvent> value) {
        root.setOnMouseClicked(value);
    }

    public Pane getRoot() {
        return root;
    }

    private Runnable refresh;

    public void setRefresh(Runnable refresh) {
        this.refresh = refresh;
    }

    public void refresh() {
        refresh.run();
    }
}

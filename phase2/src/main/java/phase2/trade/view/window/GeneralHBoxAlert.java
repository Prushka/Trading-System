package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class GeneralHBoxAlert extends AlertWindow<Void> {

    private Map<TextField, StringProperty> results = new HashMap<>();

    public GeneralHBoxAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
    }

    public void addNodes(Node... nodes) {
        body.getChildren().addAll(nodes);
    }

    private EventHandler<ActionEvent> confirmHandler;

    public void setEventHandler(EventHandler<ActionEvent> actionEventEventHandler) {
        this.confirmHandler = actionEventEventHandler;
    }

    public Void display(String... args) {

        Button confirmButton = new JFXButton("Ok");

        confirmButton.setOnAction(confirmHandler);

        confirmButton.addEventFilter(ActionEvent.ACTION, event -> {
            alert.hideWithAnimation();
        });
        confirmButton.setFocusTraversable(false);

        layout.setActions(confirmButton);

        alert.setContent(layout);


        alert.showAndWait();
        return null;
    }

}

package phase2.trade.view.window;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TextFieldAlert extends AlertWindow<Void> {

    private Map<TextField, StringProperty> results = new HashMap<>();

    public TextFieldAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
    }

    public void addTextField(TextField... textFields) {
        body.getChildren().addAll(textFields);
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

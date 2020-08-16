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

public class GeneralVBoxAlert extends AlertWindow<Void> {

    public GeneralVBoxAlert(Stage parent, String title, String header) {
        super(parent, title, header);
        body.setSpacing(35);
    }

    public Void display(String... args) {
        confirmButton.setOnAction(confirmHandler);

        layout.setActions(confirmButton);

        alert.setContent(layout);
        alert.showAndWait();
        return null;
    }

}

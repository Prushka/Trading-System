package phase2.trade.view;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConfirmWindow extends AlertWindow<Boolean> {

    public ConfirmWindow(Stage parent, String title, String header) {
        super(parent, title, header);
    }

    private Boolean answer;

    public Boolean display(String... args) {
        JFXButton yesButton = new JFXButton("Yes");
        JFXButton noButton = new JFXButton("No");
        yesButton.setOnAction(event -> {
            answer = true;
            alert.hideWithAnimation();
        });
        noButton.setOnAction(event -> {
            answer = false;
            alert.hideWithAnimation();
        });
        yesButton.setFocusTraversable(false);
        noButton.setFocusTraversable(false);
        layout.setActions(yesButton, noButton);
        alert.setContent(layout);
        alert.showAndWait();

        return answer;
    }

}

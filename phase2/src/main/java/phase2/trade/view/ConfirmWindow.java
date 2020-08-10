package phase2.trade.view;

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

public class ConfirmWindow {

    boolean answer;

    JFXAlert alert;

    private void setCenterPosition(Stage window, Stage parent) {
        double centerXPosition = parent.getX() + parent.getWidth() / 2d;
        double centerYPosition = parent.getY() + parent.getHeight() / 2d;
        window.setOnShown(ev -> {
            window.setX(centerXPosition - window.getWidth() / 2d);
            window.setY(centerYPosition - window.getHeight() / 2d);
            window.show();
        });
    }

    public boolean display(String title, String message, Stage parent) {
        alert = new JFXAlert(parent);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(true);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(message));
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

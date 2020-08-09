package phase2.trade.controller;

import com.jfoenix.animation.alert.JFXAlertAnimation;
import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmWindow {


    @FXML
    private JFXDialog dialog;

    @FXML
    private StackPane root;

    boolean answer;

    public boolean display(String title, String message) {
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); // block input events
        window.setTitle(title);
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(message);

        JFXButton yesButton = new JFXButton("Yes");
        yesButton.setButtonType(JFXButton.ButtonType.RAISED);
        yesButton.setStyle("-fx-background-color: #db4437;-fx-text-fill:WHITE;");
        JFXButton noButton = new JFXButton("No");

        noButton.setButtonType(JFXButton.ButtonType.RAISED);
        noButton.setStyle("-fx-background-color: #1b5e20;-fx-text-fill:WHITE;");

        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });

        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });

        VBox root = new VBox(20);
        root.setPrefHeight(150);

        HBox layout = new HBox(20);
        layout.getChildren().addAll(yesButton, noButton);
        layout.setAlignment(Pos.CENTER);

        root.getChildren().addAll(label, layout);

        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root);
        window.setScene(scene);
        window.showAndWait();

        return answer;
    }
}

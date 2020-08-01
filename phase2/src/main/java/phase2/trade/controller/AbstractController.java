package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public abstract class AbstractController {

    SceneFactory sceneFactory = new SceneFactory();


    void switchScene(String fileName, Object controller, ActionEvent actionEvent) {
        this.switchScene(fileName, controller, actionEvent, false);
    }
    void switchScene(String fileName, Object controller, ActionEvent actionEvent, boolean applyCSS) {
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            Scene scene = new Scene(loader.load());
            if (applyCSS) {
                scene.getStylesheets().add("css/trade.css");
            }
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

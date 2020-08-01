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
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).setScene(new Scene(loader.load()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package phase2.trade.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public abstract class AbstractController {

    SceneFactory sceneFactory = new SceneFactory();

    void switchScene(String fileName, Object controller, Stage stage, boolean applyCSS) {
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            Scene scene = new Scene(loader.load());
            if (applyCSS) {
                scene.getStylesheets().add("css/trade.css");
            }
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void switchScene(String fileName, Object controller, ActionEvent actionEvent) {
        this.switchScene(fileName, controller, actionEvent, false);
    }

    void switchScene(String fileName, Object controller, ActionEvent actionEvent, boolean applyCSS) {
        this.switchScene(fileName, controller,
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), applyCSS);
    }


    void switchScene(String fileName, Object controller, Parent parent) {
        this.switchScene(fileName, controller, parent, false);
    }

    void switchScene(String fileName, Object controller, Parent parent, boolean applyCSS) {
        this.switchScene(fileName, controller,
                (Stage) parent.getScene().getWindow(), applyCSS);
    }

    Parent loadPane(String fileName, Object controller) {
        FXMLLoader fxmlLoader = sceneFactory.getLoader(fileName);
        fxmlLoader.setController(controller);
        Parent pane = null;
        try {
            pane = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pane;
    }

}

package phase2.trade.presenter;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class SceneSwitcher {

    private final Stage window;

    private final SceneFactory sceneFactory = new SceneFactory();

    public SceneSwitcher(Stage window){
        this.window = window;
    }

    public void switchScene(String fileName, Object controller, boolean applyCSS) {
        FXMLLoader loader = sceneFactory.getLoader(fileName);
        loader.setController(controller);
        try {
            Scene scene = new Scene(loader.load());
            if (applyCSS) {
                scene.getStylesheets().add("css/trade.css");
            }
            window.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void switchScene(String fileName, Object controller) {
        this.switchScene(fileName, controller, true);
    }

}

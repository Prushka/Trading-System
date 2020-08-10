package phase2.trade.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.Main;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.presenter.SceneManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

// the resource files have a very small chance not to be indexed, cleaning build cache could solve it
public class SceneFactory {

    private final SceneManager sceneManager;

    public SceneFactory(SceneManager sceneManager){
        this.sceneManager = sceneManager;
    }

    public FXMLLoader getLoader(String fileName, Class<?> clazz) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(clazz.getResource("/fxml/" + fileName), en);
    }

    public FXMLLoader getLoader(String fileName) {
        return getLoader(fileName, Main.class);
    }

    public Parent loadPane(String fileName) {
        try {
            return getLoader(fileName).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Parent loadPane(String fileName, Object controller) {
        FXMLLoader loader = getLoader(fileName, controller.getClass());
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> Parent loadPane(String fileName, ControllerSupplier<T> controller) {
        return this.loadPane(fileName, controller.get(sceneManager));
    }
}

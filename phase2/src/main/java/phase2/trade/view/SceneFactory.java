package phase2.trade.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import phase2.trade.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneFactory {

    public FXMLLoader getLoader(String fileName, Class<?> clazz) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(clazz.getResource("/fxml/" + fileName), en);
    }

    public FXMLLoader getLoader(String fileName) {
        return getLoader(fileName, Main.class);
    }

    public Parent getPane(String fileName) {
        try {
            return getLoader(fileName).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Parent getPane(String fileName, Object controller) { // exactly same code and this one couldn't find the location
        FXMLLoader loader = getLoader(fileName, controller.getClass());
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

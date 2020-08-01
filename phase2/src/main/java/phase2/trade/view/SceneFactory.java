package phase2.trade.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import phase2.trade.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class SceneFactory {

    public FXMLLoader getLoader(String fileName) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(Main.class.getResource("/fxml/" + fileName), en);
    }

    public Parent getPane(String fileName) {
        try {
            return getLoader(fileName).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Parent getPane(String fileName, Object controller) {
        FXMLLoader loader = getLoader(fileName);
        loader.setController(controller);
        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

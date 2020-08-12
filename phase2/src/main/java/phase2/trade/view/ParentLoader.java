package phase2.trade.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import phase2.trade.Main;
import phase2.trade.presenter.ControllerSupplier;
import phase2.trade.controller.ControllerResources;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

// the resource files have a very small chance not to be indexed, cleaning build cache could solve it
public class ParentLoader {

    public ParentLoader(){}

    public FXMLLoader getLoader(String fileName, Class<?> clazz) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(clazz.getResource("/fxml/" + fileName), en);
    }

    public FXMLLoader getLoader(String fileName) {
        return getLoader(fileName, Main.class);
    }

    public Parent loadParent(String fileName) {
        try {
            return getLoader(fileName).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Parent loadParent(String fileName, Object controller) {
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

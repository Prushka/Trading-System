package phase2.trade.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import phase2.trade.Main;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Parent loader.
 *
 * @author Dan Lyu
 */
// the resource files have a very small chance not to be indexed, cleaning build cache could solve it
public class ParentLoader {

    /**
     * Gets loader.
     *
     * @param fileName the file name
     * @param clazz    the clazz
     * @return the loader
     */
    public FXMLLoader getLoader(String fileName, Class<?> clazz) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(clazz.getResource("/fxml/" + fileName), en);
    }

    /**
     * Gets loader.
     *
     * @param fileName the file name
     * @return the loader
     */
    public FXMLLoader getLoader(String fileName) {
        return getLoader(fileName, Main.class);
    }

    /**
     * Load parent parent.
     *
     * @param fileName the file name
     * @return the parent
     */
    public Parent loadParent(String fileName) {
        try {
            return getLoader(fileName).load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Load parent parent.
     *
     * @param fileName   the file name
     * @param controller the controller
     * @return the parent
     */
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

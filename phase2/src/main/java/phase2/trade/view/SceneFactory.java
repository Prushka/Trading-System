package phase2.trade.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.Main;

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

    public FXMLLoader getLoader(String fileName, Class<?> clazz) {
        Locale locale = new Locale("en", "US");
        ResourceBundle en = ResourceBundle.getBundle("language.strings", locale);

        return new FXMLLoader(clazz.getResource("/fxml/" + fileName), en);
    }

    public void switchScene(String fileName, Object controller, Stage stage, boolean applyCSS) {
        FXMLLoader loader = getLoader(fileName);
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

    public void switchScene(String fileName, Object controller, ActionEvent actionEvent) {
        this.switchScene(fileName, controller, actionEvent, false);
    }

    public void switchScene(String fileName, Object controller, ActionEvent actionEvent, boolean applyCSS) {
        this.switchScene(fileName, controller,
                (Stage) ((Node) actionEvent.getSource()).getScene().getWindow(), applyCSS);
    }


    public void switchScene(String fileName, Object controller, Parent parent) {
        this.switchScene(fileName, controller, parent, false);
    }

    public void switchScene(String fileName, Object controller, Parent parent, boolean applyCSS) {
        this.switchScene(fileName, controller,
                (Stage) parent.getScene().getWindow(), applyCSS);
    }

    public FXMLLoader getLoader(String fileName) {
        return getLoader(fileName, Main.class);
    }

    private List<String> getResourceFiles(String path) {
        List<String> filenames = new ArrayList<>();

        try (
                InputStream in = getResourceAsStream(path);
                BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String resource;

            while ((resource = br.readLine()) != null) {
                filenames.add(resource);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return filenames;
    }

    private InputStream getResourceAsStream(String resource) {
        final InputStream in
                = getContextClassLoader().getResourceAsStream(resource);

        return in == null ? getClass().getResourceAsStream(resource) : in;
    }

    private ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
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
}

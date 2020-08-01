package phase2.trade.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import phase2.trade.Main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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

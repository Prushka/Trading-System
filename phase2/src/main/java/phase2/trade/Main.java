package phase2.trade;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Locale locale = new Locale("en", "US");
        ResourceBundle bundle = ResourceBundle.getBundle("language.strings", locale);

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"),bundle);

        Scene scene = new Scene(root, 800, 500);
        primaryStage.setTitle("Trade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
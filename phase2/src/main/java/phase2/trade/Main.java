package phase2.trade;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.database.UserDAO;
import phase2.trade.repository.SaveHook;
import phase2.trade.view.SceneFactory;

import java.util.logging.Level;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        SaveHook saveHook = new SaveHook();

        SceneFactory sceneFactory = new SceneFactory();
        FXMLLoader login = sceneFactory.getLoader("login.fxml");

        LoginController loginController = new LoginController(new UserDAO());
        login.setController(loginController);


        Scene scene = new Scene(login.load());

        scene.getStylesheets().add("css/trade.css");

        primaryStage.setOnCloseRequest(event -> saveHook.save());
        primaryStage.setTitle("Trade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
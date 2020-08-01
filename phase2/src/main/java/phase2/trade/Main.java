package phase2.trade;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import phase2.trade.controller.AccountManager;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.database.Callback;
import phase2.trade.database.UserDAO;
import phase2.trade.repository.SaveHook;
import phase2.trade.user.User;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
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


        Scene scene = new Scene(sideLoadDashboard());

        primaryStage.setOnCloseRequest(event -> saveHook.save());
        primaryStage.setTitle("Trade");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent sideLoadDashboard() throws IOException {
        SceneFactory sceneFactory = new SceneFactory();
        AccountManager accountManager = new AccountManager(new UserDAO());
        accountManager.login(result -> {}, "123","123");
        DashboardController dashboardController = new DashboardController(accountManager);
        FXMLLoader fxmlLoader = sceneFactory.getLoader("personal_dashboard.fxml");
        fxmlLoader.setController(dashboardController);
        return fxmlLoader.load();
    }
}
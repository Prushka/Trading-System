package phase2.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.database.DatabaseResourceBundleImpl;
import phase2.trade.database.UserDAO;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class TradeApplication extends Application {

    private final DatabaseResourceBundle databaseResourceBundle;

    private final ShutdownHook shutdownHook;

    public TradeApplication() {
        DatabaseResourceBundleImpl databaseResourceBundle = new DatabaseResourceBundleImpl();
        shutdownHook = new ShutdownHook();
        shutdownHook.addShutdownable(databaseResourceBundle);
        this.databaseResourceBundle = databaseResourceBundle;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // mockDashboard(primaryStage);
        login(primaryStage);
    }

    private void login(Stage primaryStage) throws IOException {

        SceneFactory sceneFactory = new SceneFactory();
        FXMLLoader login = sceneFactory.getLoader("login.fxml");

        LoginController loginController = new LoginController(databaseResourceBundle);
        login.setController(loginController);
        primaryStage.setTitle("Trade");
        primaryStage.setScene(new Scene(login.load()));
        primaryStage.show();
    }

    private void mockDashboard(Stage primaryStage) {
        SceneFactory sceneFactory = new SceneFactory();
        AccountManager accountManager = new AccountManager(databaseResourceBundle.getUserDAO());
        accountManager.login(result -> {
            DashboardController dashboardController = new DashboardController(databaseResourceBundle, accountManager);
            Platform.runLater(() -> {

                Parent dashboard = sceneFactory.getPane("personal_dashboard.fxml", dashboardController);
                Scene scene = new Scene(dashboard);

                scene.getStylesheets().add("css/trade.css");

                primaryStage.setScene(scene);
                primaryStage.show();
            });

        }, "aaa@bbb.ccc", "12345678");
    }

    @Override
    public void stop() {
        shutdownHook.shutdown();
    }

}

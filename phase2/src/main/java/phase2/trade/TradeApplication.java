package phase2.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class TradeApplication extends Application {

    private final GatewayBundle gatewayBundle;

    private final ShutdownHook shutdownHook;

    public TradeApplication() {
        DatabaseResourceBundle databaseResourceBundle = new DatabaseResourceBundle();
        shutdownHook = new ShutdownHook();
        shutdownHook.addShutdownable(databaseResourceBundle);
        EntityBundle entityBundle = new DatabaseResourceBundle().getDaoBundle();
        ConfigBundle configBundle = new ConfigBundle();
        this.gatewayBundle = new GatewayBundle(entityBundle, configBundle);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // mockDashboard(primaryStage);
        login(primaryStage);
    }

    private void login(Stage primaryStage) throws IOException {

        SceneFactory sceneFactory = new SceneFactory();
        FXMLLoader login = sceneFactory.getLoader("login.fxml");

        LoginController loginController = new LoginController(gatewayBundle);
        login.setController(loginController);
        primaryStage.setTitle("Trade");
        primaryStage.setScene(new Scene(login.load()));
        primaryStage.show();
    }

    private void mockDashboard(Stage primaryStage) {
        SceneFactory sceneFactory = new SceneFactory();
        AccountManager accountManager = new AccountManager(gatewayBundle.getEntityBundle());
        accountManager.login(result -> {
            DashboardController dashboardController = new DashboardController(gatewayBundle, accountManager);
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

package phase2.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.gateway.ConfigBundle;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.AccountManager;
import phase2.trade.user.CreateHeadIfNotExist;
import phase2.trade.view.SceneFactory;

import java.io.IOException;

public class TradeApplication extends Application {

    private final GatewayBundle gatewayBundle;

    private final ShutdownHook shutdownHook;

    public TradeApplication() {

        shutdownHook = new ShutdownHook();

        ConfigBundle configBundle = new ConfigBundle();

        DatabaseResourceBundle databaseResourceBundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig());
        EntityBundle entityBundle = databaseResourceBundle.getDaoBundle();

        shutdownHook.addShutdownable(databaseResourceBundle, configBundle);

        this.gatewayBundle = new GatewayBundle(entityBundle, configBundle);
    }

    private void loadFont(String name) {
        Font font = Font.loadFont(
                this.getClass().getResource("/font/" + name + ".ttf").toExternalForm(),
                10
        );
        System.out.println(font.getFamily());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // mockDashboard(primaryStage);
        loadFont("OpenSans");
        loadFont("OpenSansM");
        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/test.png")));
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
        AccountManager accountManager = new AccountManager(gatewayBundle);
        accountManager.login((result, status) -> {
            DashboardController dashboardController = new DashboardController(gatewayBundle, accountManager);
            Platform.runLater(() -> {

                Parent dashboard = sceneFactory.loadPane("dashboard.fxml", dashboardController);
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

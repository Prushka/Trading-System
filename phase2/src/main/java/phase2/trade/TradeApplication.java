package phase2.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.controller.DashboardController;
import phase2.trade.controller.LoginController;
import phase2.trade.database.UserDAO;
import phase2.trade.repository.SaveHook;
import phase2.trade.user.AccountManager;
import phase2.trade.user.User;
import phase2.trade.view.SceneFactory;

import java.io.IOException;
import java.util.logging.Level;

public class TradeApplication extends Application {

    private final SessionFactory sessionFactory;

    public TradeApplication() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.configure().buildSessionFactory();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // mockDashboard(primaryStage);
        login(primaryStage);
    }

    private void login(Stage primaryStage) throws IOException {

        SceneFactory sceneFactory = new SceneFactory();
        FXMLLoader login = sceneFactory.getLoader("login.fxml");

        LoginController loginController = new LoginController(new UserDAO(sessionFactory));
        login.setController(loginController);
        primaryStage.setTitle("Trade");
        primaryStage.setScene(new Scene(login.load()));
        primaryStage.show();
    }

    private void mockDashboard(Stage primaryStage) {
        SceneFactory sceneFactory = new SceneFactory();
        AccountManager accountManager = new AccountManager(new UserDAO(sessionFactory));
        accountManager.login(result -> {
            DashboardController dashboardController = new DashboardController(accountManager);
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
        sessionFactory.close();
        System.exit(0);
        // hibernate in single thread exits normally with 0
        // while doing multiple threads, the hibernate part will block the termination even the factory and sessions are all closed.
        // No answer can be found.
    }

}

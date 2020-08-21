package phase2.trade;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import phase2.trade.user.controller.LoginController;

public class TradeApplication extends Application {

    private static final Logger logger = LogManager.getLogger(TradeApplication.class);

    private final Configurer configurer = new Configurer();

    private void loadFont(String name) {
        Font font = Font.loadFont(
                this.getClass().getResource("/font/" + name + ".ttf").toExternalForm(),
                10
        );
        logger.info("Loaded Font: " + font.getFamily());
    }

    // TODO:
    //  VALIDATION
    //  Avoid SQL injection
    //  Avoid spaces

    @Override
    public void start(Stage primaryStage) {
        loadFont("OpenSans");
        loadFont("OpenSansM");

        configurer.configure(primaryStage);

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/test.png")));

        //configurer.mockRegister("justsomeuser","a@b.ccc", "12345678");
        //configurer.mockLogin("justsomeuser", "12345678");
        // login(primaryStage);
        //configurer.mockLogin("admin", "admin???");

        //configurer.testTrade();
        //configurer.loginRestaurant();
        //configurer.getControllerResources().getSceneManager().switchScene(DashboardController::new);
        login(primaryStage);
        primaryStage.show();
    }

    private void login(Stage primaryStage) {
        configurer.getControllerResources().getSceneManager().switchScene(LoginController::new);
        primaryStage.setTitle("Trade");
        primaryStage.show();
    }


    @Override
    public void stop() {
        configurer.getShutdownHook().shutdown();
    }

}

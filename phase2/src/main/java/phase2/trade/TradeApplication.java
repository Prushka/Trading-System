package phase2.trade;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import phase2.trade.user.controller.LoginController;

/**
 * The Trade application.
 *
 * @author Dan Lyu
 */
public class TradeApplication extends Application {

    private final Configurer configurer = new Configurer();

    /*
    private void loadFont(String name) {
        Font font = Font.loadFont(
                this.getClass().getResource("/font/" + name + ".ttf").toExternalForm(),
                10
        );
        logger.info("Loaded Font: " + font.getFamily());
    }*/

    @Override
    public void start(Stage primaryStage) {
        //loadFont("OpenSans");
        //loadFont("OpenSansM");

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

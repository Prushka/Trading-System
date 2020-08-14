package phase2.trade;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import phase2.trade.controller.DashboardController;
import phase2.trade.user.controller.LoginController;

public class TradeApplication extends Application {

    private final Configurer configurer = new Configurer();

    private void loadFont(String name) {
        Font font = Font.loadFont(
                this.getClass().getResource("/font/" + name + ".ttf").toExternalForm(),
                10
        );
        System.out.println(font.getFamily());
    }

    // TODO:
    //  VALIDATION!
    //  Avoid SQL injection
    //  Avoid spaces

    @Override
    public void start(Stage primaryStage) {
        loadFont("OpenSans");
        loadFont("OpenSansM");

        configurer.configure(primaryStage);

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/test.png")));

        // configurer.mockDashboardRegister("justsomeuser", "a@b.ccc");
        // login(primaryStage);
        // configurer.getControllerResources().getSceneManager().switchScene(DashboardController::new);
        configurer.mockDashboardLogin("admin", "admin???");
        configurer.getControllerResources().getSceneManager().switchScene(DashboardController::new);
        //login(primaryStage);
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

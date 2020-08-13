package phase2.trade;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import phase2.trade.command.Command;
import phase2.trade.controller.DashboardController;
import phase2.trade.user.controller.LoginController;
import phase2.trade.config.ConfigBundle;
import phase2.trade.gateway.database.DatabaseResourceBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.controller.ControllerResources;
import phase2.trade.user.AccountManager;

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

        configurer.mockDashboardRegister("cannot-catch-any-hope", "password");
        // mockDashboardLogin(primaryStage, "admin", "admin???");
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

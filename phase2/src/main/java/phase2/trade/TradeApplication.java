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

    private ControllerResources controllerResources;

    private final GatewayBundle gatewayBundle;

    private final ShutdownHook shutdownHook;

    public TradeApplication() {

        shutdownHook = new ShutdownHook();

        ConfigBundle configBundle = new ConfigBundle();
        DatabaseResourceBundle databaseResourceBundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig());

        shutdownHook.addShutdownable(databaseResourceBundle, configBundle);

        gatewayBundle = new GatewayBundle(databaseResourceBundle.getDaoBundle(), configBundle);
    }

    private void loadFont(String name) {
        Font font = Font.loadFont(
                this.getClass().getResource("/font/" + name + ".ttf").toExternalForm(),
                10
        );
        System.out.println(font.getFamily());
    }

    @Override
    public void start(Stage primaryStage) {
        loadFont("OpenSans");
        loadFont("OpenSansM");

        controllerResources = new ControllerResources(gatewayBundle, primaryStage, new AccountManager(gatewayBundle));

        primaryStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/test.png")));


        new CreatePrerequisiteIfNotExist(controllerResources.getCommandFactory());
        // mockDashboardRegister(primaryStage);
        mockDashboardLogin(primaryStage, "admin", "admin???");
        //login(primaryStage);
    }

    private void login(Stage primaryStage) {
        controllerResources.getSceneManager().switchScene(LoginController::new);
        primaryStage.setTitle("Trade");
        primaryStage.show();
    }

    private void addExampleItems(String name, String description, Category category, int quantity, double price) {
        Command<Item> itemCommand = controllerResources.getCommandFactory().getCommand(AddItemToItemList::new, c -> {
            c.setItemListType(ItemListType.INVENTORY);
            c.setAsynchronous(false);
        });
        itemCommand.execute((result, status) -> {
        }, name, description, category.name(), String.valueOf(quantity), String.valueOf(price));
    }

    private void mockDashboardRegister(Stage primaryStage, String username, String password) {
        controllerResources.getAccountManager().register((result, status) -> {
            Platform.runLater(() -> {
                addExample();
                controllerResources.getSceneManager().switchScene(DashboardController::new);
                primaryStage.show();
            });

        }, username, password, "12345678", "country", "city");
    }

    private void mockDashboardLogin(Stage primaryStage, String userName, String password) {
        controllerResources.getAccountManager().login((result, status) -> {
            Platform.runLater(() -> {
                controllerResources.getSceneManager().switchScene(DashboardController::new);
                primaryStage.show();
            });

        }, userName, password);
    }

    private void addExample() {

        addExampleItems("Weathering With You", "A boy runs away to Tokyo and befriends a girl who appears to be able to manipulate the weather.", Category.MOVIE, 4, -1);
        addExampleItems("Ulysses", "Ulysses is a modernist novel by Irish writer James Joyce.", Category.BOOK, 2, -1);
        addExampleItems("Broken iPad", "An ipad that's melted.", Category.ELECTRONIC, 1, 1000);
        addExampleItems("Queen Bed", "No description", Category.FURNITURE, 2, 1000);

    }

    @Override
    public void stop() {
        shutdownHook.shutdown();
    }

}

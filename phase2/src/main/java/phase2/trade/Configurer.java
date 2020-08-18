package phase2.trade;

import javafx.stage.Stage;
import phase2.trade.command.Command;
import phase2.trade.command.CommandFactory;
import phase2.trade.config.ConfigBundle;
import phase2.trade.controller.ControllerResources;
import phase2.trade.database.DatabaseResourceBundle;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.itemlist.ItemListType;
import phase2.trade.user.AccountManager;

// Use this in TradeApplication or Test
// Make sure the asynchronous variable in DAO is set to false if you are mocking anything here
public class Configurer {

    private ControllerResources controllerResources;

    private final GatewayBundle gatewayBundle;

    private final ShutdownHook shutdownHook;

    private final CommandFactory commandFactory;

    private final AccountManager accountManager;

    public Configurer() {
        shutdownHook = new ShutdownHook();

        ConfigBundle configBundle = new ConfigBundle();
        DatabaseResourceBundle databaseResourceBundle = new DatabaseResourceBundle(configBundle.getDatabaseConfig());

        shutdownHook.addShutdownables(databaseResourceBundle, configBundle);
        gatewayBundle = new GatewayBundle(databaseResourceBundle.getEntityGatewayBundle(), configBundle);

        accountManager = new AccountManager(gatewayBundle);
        commandFactory = new CommandFactory(gatewayBundle, accountManager);
    }

    public void configure(Stage primaryStage) {
        controllerResources = new ControllerResources(gatewayBundle, shutdownHook, primaryStage, accountManager);

        new CreatePrerequisiteIfNotExist(getControllerResources().getCommandFactory());
    }

    public CommandFactory getCommandFactory() {
        return commandFactory;
    }

    public ControllerResources getControllerResources() {
        return controllerResources;
    }

    public ShutdownHook getShutdownHook() {
        return shutdownHook;
    }


    private void addExample() {
        // addExampleItems("Weathering With You", "A boy runs away to Tokyo and befriends a girl who appears to be able to manipulate the weather.", Category.MOVIE, 4, -1);
        // addExampleItems("Ulysses", "Ulysses is a modernist novel by Irish writer James Joyce.", Category.BOOK, 2, -1);
        // addExampleItems("Broken iPad", "An ipad that's melted.", Category.ELECTRONIC, 1, 1000);
        // addExampleItems("Queen Bed", "No description", Category.FURNITURE, 2, 1000);
    }

    private void addExampleItems(String name, String description, Category category, Willingness willingness, int quantity, double price) {
        Command<Item> itemCommand = getCommandFactory().getCommand(AddItemToItemList::new, c -> {
            c.setItemListType(ItemListType.INVENTORY);
            c.setAsynchronous(false);
        });
        itemCommand.execute((result, status) -> {
        }, name, description, category.name(), String.valueOf(quantity), willingness.name(), String.valueOf(price));
    }

    public void mockRegister(String username, String email, String password) {
        accountManager.register((result, status) -> {

        }, username, email, password, "Canada", "Ontario", "Toronto");

        addExample();

    }

    public void mockLogin(String userName, String password) {
        accountManager.login((result, status) -> {

        }, userName, password);
    }

    public static void main(String[] args) {
        Configurer configurer = new Configurer();
        configurer.mockRegister("admin2", "admin???", "12345678");
        configurer.getCommandFactory().getCommand(AddItemToItemList::new);

    }

    public void testTrade() {
        mockRegister("HumanTrafficker", "sell@humans.we", "12345678");
        addExampleItems("A Boy", "This is a Boy", Category.EQUIPMENT, Willingness.SELL, 1, 20);
        addExampleItems("A Girl", "This is a Girl", Category.MISCELLANEOUS, Willingness.LEND, 1, 20);
        accountManager.logOut();

        mockRegister("Amazon", "sell@trash.we", "12345678");
        addExampleItems("Hazardous Waste", "This is Hazardous Waste", Category.ELECTRONIC,
                Willingness.SELL, 1000, 20000);
        addExampleItems("Medical Waste", "This is Medical Waste", Category.MOVIE,
                Willingness.SELL, 1000, 20000);
        accountManager.logOut();

        mockRegister("Restaurant", "sell@food.we", "12345678");
        addExampleItems("Sushi", "This is Sushi", Category.FOOD, Willingness.SELL, 1, 20);
        addExampleItems("Salmon", "This is Salmon", Category.FOOD, Willingness.LEND, 1, 20);

    }

    public void loginRestaurant() {
        mockLogin("Restaurant", "12345678");
    }
}

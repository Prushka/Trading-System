package group.system;

import group.config.LoggerFactory;
import group.config.property.TradeProperties;
import group.item.Item;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

import java.util.logging.Logger;

public class ControllerDispatcher implements Shutdownable {

    static final Logger LOGGER = new LoggerFactory(ControllerDispatcher.class).getConfiguredLogger();

    Repository<SupportTicket> ticketRepository;
    Repository<PersonalUser> personalUserRepository;
    Repository<AdministrativeUser> adminUserRepository;
    Repository<Trade> tradeRepository;
    Repository<Item> itemRepository;

    SupportTicketController supportTicketController;
    UserController userController;
    AdministrativeUserController administrativeUserController;
    TradeController testTradeController;

    TradeProperties tradeProperties;

    final MenuController menuController = new MenuController();

    private final SaveHook saveHook = new SaveHook();

    public ControllerDispatcher() {
        createProperties();
        createRepositories();
        dispatchController();
        MenuRunner menuRunner = new MenuRunner(menuController.generateMenuLogicController());
        menuRunner.shutdownHook(this);
        menuRunner.run();
    }

    public void dispatchController() {
        supportTicketController = new SupportTicketController(this);
        userController = new UserController(this);
        testTradeController = new TradeController(this);
        administrativeUserController = new AdministrativeUserController(this);
        this.menuController.mainMenu(userController, administrativeUserController); // these steps should not be here
        this.menuController.adminUserAccess(administrativeUserController);
        this.menuController.adminUserLimitAccess(administrativeUserController);
        this.menuController.adminUserAddItemAccess(administrativeUserController);
        this.menuController.adminUserUnfreezeAccess(administrativeUserController);
        this.menuController.adminUserFreezeAccess(administrativeUserController);
    }

    public void createRepositories() {
        ticketRepository = new CSVRepository<>("data/support_ticket.csv", SupportTicket::new, saveHook);
        personalUserRepository = new CSVRepository<>("data/personal_user.csv", PersonalUser::new, saveHook);
        adminUserRepository = new CSVRepository<>("data/admin_user.csv", AdministrativeUser::new, saveHook);
        tradeRepository = new CSVRepository<>("data/trade.csv", Trade::new, saveHook);
        itemRepository = new CSVRepository<>("data/item.csv", Item::new, saveHook);
    }

    public void createProperties() {
        tradeProperties = new TradeProperties(saveHook);
    }

    public void shutdown() {
        saveHook.save();
    }
}

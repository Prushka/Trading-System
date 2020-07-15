package group.system;

import group.config.property.TradeProperties;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;
import group.item.Item;

public class ControllerDispatcher implements Shutdownable {

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

    final MenuConstructor menuConstructor = new MenuConstructor();

    private final SaveHook saveHook = new SaveHook();

    public ControllerDispatcher() {
        menuConstructor.shutdownHook(this);
        createProperties();
        createRepositories();
        dispatchController();
        menuConstructor.runMenu();
    }

    public void dispatchController() {
        supportTicketController = new SupportTicketController(this);
        userController = new UserController(this);
        testTradeController = new TradeController(this);
        administrativeUserController = new AdministrativeUserController(this);
        this.menuConstructor.mainMenu(userController, administrativeUserController);
        this.menuConstructor.adminUserAccess(administrativeUserController);
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

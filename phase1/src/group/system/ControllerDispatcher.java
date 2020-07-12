package group.system;

import group.config.property.TradeProperties;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class ControllerDispatcher implements Shutdownable {

    Repository<SupportTicket> ticketRepository;
    Repository<PersonalUser> personalUserRepository;
    Repository<AdministrativeUser> adminUserRepository;
    Repository<Trade> tradeRepository;

    SupportTicketController supportTicketController;
    UserController userController;
    // TODO: remove grace code
    TestTradeController testTradeController;

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
        // TODO: remove grace code
        testTradeController = new TestTradeController(this);
    }

    public void createRepositories() {
        ticketRepository = new CSVRepository<>("data/support_ticket.csv", SupportTicket::new, saveHook);
        personalUserRepository = new CSVRepository<>("data/personal_user.csv", PersonalUser::new, saveHook);
        adminUserRepository = new CSVRepository<>("data/admin_user.csv", AdministrativeUser::new, saveHook);
        tradeRepository = new CSVRepository<>("data/trade.csv", Trade::new, saveHook);
    }

    public void createProperties() {
        TradeProperties tradeProperties = new TradeProperties(saveHook);
    }

    public void shutdown() {
        saveHook.save();
    }
}

package group.system;

import group.config.property.TradeProperties;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.RepositorySavable;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class ControllerDispatcher implements Shutdownable {

    RepositorySavable<SupportTicket> ticketRepository;
    RepositorySavable<PersonalUser> personalUserRepository;
    RepositorySavable<AdministrativeUser> adminUserRepository;
    RepositorySavable<Trade> tradeRepository;

    SupportTicketController supportTicketController;
    UserController userController;

    TradeProperties tradeProperties = new TradeProperties();

    final MenuConstructor menuConstructor = new MenuConstructor();

    private final SaveHook saveHook = new SaveHook();

    public ControllerDispatcher() {
        menuConstructor.shutdownHook(this);
        createRepositories();
        dispatchController();
        menuConstructor.runMenu();
    }

    public void dispatchController() {
        supportTicketController = new SupportTicketController(this);
        userController = new UserController(this);
    }

    public void createRepositories() {
        ticketRepository = new CSVRepository<>("data/support_ticket.csv", SupportTicket::new, saveHook);
        personalUserRepository = new CSVRepository<>("data/personal_user.csv", PersonalUser::new, saveHook);
        adminUserRepository = new CSVRepository<>("data/admin_user.csv", AdministrativeUser::new, saveHook);
        tradeRepository = new CSVRepository<>("data/trade.csv", Trade::new, saveHook);
    }

    public void shutdown() {
        saveHook.save();
    }
}

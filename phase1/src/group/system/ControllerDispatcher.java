package group.system;

import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

public class ControllerDispatcher implements Shutdownable {

    CSVRepository<SupportTicket> ticketRepository;
    CSVRepository<PersonalUser> personalUserRepository;
    CSVRepository<AdministrativeUser> adminUserRepository;
    CSVRepository<Trade> tradeRepository;

    SupportTicketController supportTicketController;
    UserController userController;

    MenuConstructor menuConstructor;

    private SaveHook saveHook;

    public ControllerDispatcher() {
        menuConstructor = new MenuConstructor();
        menuConstructor.shutdownHook(this);
        createRepositories();
        dispatchController();
        menuConstructor.buildMenu();
    }

    public void dispatchController() {
        supportTicketController = new SupportTicketController(this);
        userController = new UserController(this);
    }

    public void createRepositories() {
        saveHook = new SaveHook();
        ticketRepository = new CSVRepository<>("data/support_ticket.csv", SupportTicket::new, saveHook);
        personalUserRepository = new CSVRepository<>("data/personal_user.csv", PersonalUser::new, saveHook);
        adminUserRepository = new CSVRepository<>("data/admin_user.csv", AdministrativeUser::new, saveHook);
        tradeRepository = new CSVRepository<>("data/trade.csv", Trade::new, saveHook);
    }

    public void shutdown() {
        saveHook.save();
    }
}

package com.phase2.trade.system;

import com.phase2.trade.config.SaveHook;
import group.config.property.SystemProperties;
import group.config.property.TradeProperties;
import group.item.Item;
import group.notification.SupportTicket;
import group.repository.CSVRepository;
import group.repository.Repository;
import group.trade.Trade;
import group.user.AdministrativeUser;
import group.user.PersonalUser;

/**
 * A wrapper class for all Controllers, Repositories and Properties.<p>
 * This class dispatches them.
 */
public class ControllerDispatcher implements Shutdownable {

    /**
     * The repository that holds {@link SupportTicket}
     */
    Repository<SupportTicket> ticketRepository;

    /**
     * The repository that holds {@link PersonalUser}
     */
    Repository<PersonalUser> personalUserRepository;

    /**
     * The repository that holds {@link AdministrativeUser}
     */
    Repository<AdministrativeUser> adminUserRepository;

    /**
     * The repository that holds {@link Trade}
     */
    Repository<Trade> tradeRepository;

    /**
     * The repository that holds {@link Item}
     */
    Repository<Item> itemRepository;

    SupportTicketController supportTicketController;
    UserController userController;
    AdministrativeUserController administrativeUserController;
    TradeController tradeController;

    TradeProperties tradeProperties;
    SystemProperties systemProperties;

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
        administrativeUserController = new AdministrativeUserController(this);
        supportTicketController = new SupportTicketController(this);
        userController = new UserController(this);
        tradeController = new TradeController(this);
        menuController.viewAccount(userController, tradeController);
        this.menuController.mainMenu(userController, administrativeUserController); // these steps should not be here
        this.menuController.personalUserAccess(userController, supportTicketController);
        this.menuController.supportTrade(tradeController);
        this.menuController.adminUserAccess(administrativeUserController);
        this.menuController.adminUserLimitAccess(administrativeUserController);
        this.menuController.adminUserAddItemAccess(administrativeUserController);
        this.menuController.adminUserUnfreezeAccess(administrativeUserController);
        this.menuController.adminUserFreezeAccess(administrativeUserController);
    }

    /**
     * Initialize all Repositories
     */
    public void createRepositories() {
        ticketRepository = new CSVRepository<>("data/support_ticket.csv", SupportTicket::new, saveHook);
        personalUserRepository = new CSVRepository<>("data/personal_user.csv", PersonalUser::new, saveHook);
        adminUserRepository = new CSVRepository<>("data/admin_user.csv", AdministrativeUser::new, saveHook);
        tradeRepository = new CSVRepository<>("data/com.phase2.trade.trade.csv", Trade::new, saveHook);
        itemRepository = new CSVRepository<>("data/com.phase2.com.phase2.trade.trade.item.csv", Item::new, saveHook);
    }

    /**
     * Initialize all Properties
     */
    public void createProperties() {
        tradeProperties = new TradeProperties(saveHook);
        systemProperties = new SystemProperties(saveHook);
    }

    /**
     * The shutdowable shutdown
     */
    @Override
    public void shutdown() {
        saveHook.save();
    }
}

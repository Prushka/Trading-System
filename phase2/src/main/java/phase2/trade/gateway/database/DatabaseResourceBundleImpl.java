package phase2.trade.gateway.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.Shutdownable;
import phase2.trade.gateway.CommandGateway;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.gateway.TradeGateway;
import phase2.trade.gateway.UserGateway;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class DatabaseResourceBundleImpl implements Shutdownable, DatabaseResourceBundle {

    private final ExecutorService threadPool;

    private final SessionFactory sessionFactory;

    private final UserDAO userDAO;

    private final ItemDAO itemDAO;

    private final TradeDAO tradeDAO;

    private final CommandDAO commandDAO;

    public DatabaseResourceBundleImpl() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.configure().buildSessionFactory();
        threadPool = Executors.newFixedThreadPool(10); // do we need to configure this

        userDAO = new UserDAO(this);

        itemDAO = new ItemDAO(this);

        tradeDAO = new TradeDAO(this);

        commandDAO = new CommandDAO(this);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void stop() {
        sessionFactory.close();
        threadPool.shutdown();
    }

    public UserGateway getUserGateway() {
        return userDAO;
    }

    public ItemGateway getItemGateway() {
        return itemDAO;
    }

    public TradeGateway getTradeGateway() {
        return tradeDAO;
    }

    @Override
    public CommandGateway getCommandGateway() {
        return commandDAO;
    }
}

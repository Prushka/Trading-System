package phase2.trade.gateway.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.Shutdownable;
import phase2.trade.gateway.CommandGateway;
import phase2.trade.gateway.ItemGateway;
import phase2.trade.gateway.UserGateway;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class DatabaseResourceBundle implements Shutdownable {

    private final ExecutorService threadPool;

    private final SessionFactory sessionFactory;

    private final DAOBundle daoBundle;

    public DatabaseResourceBundle() {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.configure().buildSessionFactory();
        threadPool = Executors.newFixedThreadPool(10); // do we need to configure this

        daoBundle = new DAOBundle(this);
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

    public DAOBundle getDaoBundle() {
        return daoBundle;
    }
}

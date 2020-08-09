package phase2.trade.gateway.database;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.Shutdownable;
import phase2.trade.config.DatabaseConfig;
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

    public DatabaseResourceBundle(DatabaseConfig databaseConfig) {
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");

        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", databaseConfig.getUrl());
        configuration.setProperty("hibernate.connection.username", databaseConfig.getUsername());
        configuration.setProperty("hibernate.connection.password", databaseConfig.getPassword());
        configuration.setProperty("hibernate.hbm2ddl.auto", databaseConfig.getHbm2ddl());
        configuration.setProperty("hibernate.connection.autoReconnect", String.valueOf(databaseConfig.isAutoReconnect()));
        configuration.setProperty("hibernate.show_sql", String.valueOf(databaseConfig.isShowSQL()));
        configuration.setProperty("hibernate.connection.pool_size", String.valueOf(databaseConfig.getConnection_pool_size()));

        sessionFactory = configuration.configure().buildSessionFactory();
        threadPool = Executors.newFixedThreadPool(databaseConfig.getConnection_pool_size()); // do we need to configure this

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

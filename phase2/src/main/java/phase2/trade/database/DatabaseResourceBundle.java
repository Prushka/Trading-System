package phase2.trade.database;

import org.hibernate.SessionFactory;

import java.util.concurrent.ExecutorService;

public interface DatabaseResourceBundle extends GatewayBundle {

    SessionFactory getSessionFactory();

    ExecutorService getThreadPool();
}

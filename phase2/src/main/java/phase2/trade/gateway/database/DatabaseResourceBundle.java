package phase2.trade.gateway.database;

import org.hibernate.SessionFactory;
import phase2.trade.gateway.GatewayBundle;

import java.util.concurrent.ExecutorService;

public interface DatabaseResourceBundle extends GatewayBundle {

    SessionFactory getSessionFactory();

    ExecutorService getThreadPool();
}

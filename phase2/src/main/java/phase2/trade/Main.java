package phase2.trade;


import javafx.application.Application;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import phase2.trade.database.UserDAO;
import phase2.trade.user.User;

import java.util.logging.Level;

public class Main {

    private static SessionFactory sessionFactory;

    public static void main(String[] args) {
        // java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.OFF);
        // Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        // sessionFactory = configuration.configure().buildSessionFactory();
        Application.launch(TradeApplication.class, args);
    }
}
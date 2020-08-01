package com.phase2.trade.database;

import com.phase2.trade.user.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import javax.security.auth.login.Configuration;
import java.io.Serializable;
import java.util.List;

public class UserDAO {

    private Session currentSession;

    private Transaction currentTransaction;

    public UserDAO(){
    }

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionWithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionWithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    private static SessionFactory getSessionFactory() {
        return new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
    }

    public Session getCurrentSession() {
        return currentSession;
    }

    public void setCurrentSession(Session currentSession) {
        this.currentSession = currentSession;
    }

    public Transaction getCurrentTransaction() {
        return currentTransaction;
    }

    public void setCurrentTransaction(Transaction currentTransaction) {
        this.currentTransaction = currentTransaction;
    }

    public void add(User entity) {
        getCurrentSession().save(entity);
    }

    public void update(User entity) {
        getCurrentSession().update(entity);
    }

    public User findById(String id) {
        return getCurrentSession().get(User.class, id);
    }

    public void delete(User entity) {
        getCurrentSession().delete(entity);
    }

    @SuppressWarnings("unchecked")
    public List<User> findAll() {
        return (List<User>) getCurrentSession().createQuery("from User").list();
    }

    public void deleteAll() {
        List<User> entityList = findAll();
        for (User entity : entityList) {
            delete(entity);
        }
    }
}
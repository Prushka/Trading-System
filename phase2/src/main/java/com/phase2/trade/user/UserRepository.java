package com.phase2.trade.user;

import com.phase2.trade.database.Callback;
import com.phase2.trade.database.TradeDb;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private AdministrativeUserDao adminDao;
    private UserDao userDao;

    private final Executor executor;

    public UserRepository(TradeDb tradeDb) {
        this.adminDao = tradeDb.administrativeUserDao();
        this.userDao = tradeDb.userDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insert(AdministrativeUser administrativeUser, Callback<Boolean> callback) {
        getExecutor().execute(() -> {
            if(ifExists(administrativeUser.getEmail(),administrativeUser.getTelephone())) {
                callback.call(false);
            }else{
                adminDao.insertAll(administrativeUser);
                callback.call(true);
            }
        });
    }

    public void getUser(Callback<User> callback) {
        getExecutor().execute(() -> {
            callback.call(adminDao.getAll().get(0));
        });
    }

    private boolean ifExists(String email, String telephone) {
        return userDao.ifExists(email, telephone).size() > 0;
    }

    public Executor getExecutor() {
        return executor;
    }
}

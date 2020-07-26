package com.phase2.trade.user;

import com.phase2.trade.database.Callback;
import com.phase2.trade.database.UserDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class UserRepository {

    private UserDatabase userDatabase;

    private final Executor executor;

    public UserRepository(UserDatabase userDatabase){
        this.userDatabase = userDatabase;
        this.executor = Executors.newSingleThreadExecutor();
    }

    public void insertTask() {
        AdministrativeUser administrativeUser = new AdministrativeUser("name","a@a.a","12345678","1234565",true);
        insertTask(administrativeUser);
    }

    public void insertTask(AdministrativeUser administrativeUser) {
        getExecutor().execute(() -> {
            userDatabase.administrativeUserDao().insertAll(administrativeUser);
        });
    }

    public void getUser(Callback<User> callback) {
        getExecutor().execute(() -> {
            callback.call(userDatabase.administrativeUserDao().getAll().get(0));
        });
    }

    public Executor getExecutor() {
        return executor;
    }
}

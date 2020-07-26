package com.phase2.trade.user;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Room;

import com.phase2.trade.database.UserDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {

    private UserDatabase userDatabase;

    private final ExecutorService threadPool;

    public UserRepository(UserDatabase userDatabase){
        this.userDatabase = userDatabase;
        this.threadPool = Executors.newFixedThreadPool(10); // thread pool is not necessary for sqlite, remove it later
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public void insertTask() {
        AdministrativeUser administrativeUser = new AdministrativeUser("name","a@a.a","12345678","1234565",true);
        insertTask(administrativeUser);
    }

    public void insertTask(AdministrativeUser administrativeUser) {
        getThreadPool().submit(() -> {
            userDatabase.administrativeUserDao().insertAll(administrativeUser);
        });
    }
}

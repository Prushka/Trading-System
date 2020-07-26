package com.phase2.trade.user;


public class UserManager {


    private AppDatabase db;

    public UserManager(AppDatabase db){
        this.db = db;
    }

    public void userTest(){
        db.userDao().insertAll(new User("name","aha"));
    }
}

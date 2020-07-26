package com.phase2.trade.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.phase2.trade.user.AdministrativeUser;
import com.phase2.trade.user.AdministrativeUserDao;

@Database(entities = {AdministrativeUser.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract AdministrativeUserDao userDao();


}
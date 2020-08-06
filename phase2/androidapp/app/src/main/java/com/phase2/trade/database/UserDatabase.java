package com.phase2.trade.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.phase2.trade.user.AdministrativeUser;
import com.phase2.trade.user.AdministrativeUserDao;
import com.phase2.trade.user.User;

@Database(entities = {AdministrativeUser.class, User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract AdministrativeUserDao administrativeUserDao();


}
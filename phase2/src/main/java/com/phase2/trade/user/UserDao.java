package com.phase2.trade.user;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE userName LIKE :userName LIMIT 1")
    User findByName(String userName);

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<User> findAllUsersToFreeze(int[] userIds);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM user WHERE email == :email OR telephone == :telephone")
    List<User> ifExists(String email, String telephone);
}
package com.phase2.trade.user;


import android.util.Log;

import com.phase2.trade.database.Callback;
import com.phase2.trade.database.UserDatabase;

public class UserManager {

    private static final String TAG = "UserManager";

    private UserRepository userRepository;

    public UserManager(UserDatabase userDatabase){
        this.userRepository = new UserRepository(userDatabase);
    }

    public void userTest(){
        userRepository.insertTask();
        userRepository.getUser(new Callback<User>() {
            @Override
            public void call(User result) {
                Log.v(TAG, result.getUserName());
            }
        });
    }
}

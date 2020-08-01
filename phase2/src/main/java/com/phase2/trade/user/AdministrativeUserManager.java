package com.phase2.trade.user;


import android.util.Log;

import com.phase2.trade.database.Callback;
import com.phase2.trade.database.TradeDb;

public class AdministrativeUserManager {

    private static final String TAG = "AdminUserManager";

    private UserRepository userRepository;

    public AdministrativeUserManager(TradeDb tradeDb) {
        this.userRepository = new UserRepository(tradeDb);
    }

    public void register(String username, String email, String telephone, String password, boolean isHead) {
        userRepository.insert(new AdministrativeUser(username, email, telephone, password, isHead),
                new Callback<Boolean>() {
                    @Override
                    public void call(Boolean result) {
                        Log.v(TAG, result.getUserName());
                    }
                });
    }
}

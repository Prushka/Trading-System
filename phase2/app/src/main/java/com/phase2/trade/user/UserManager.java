package com.phase2.trade.user;


import com.phase2.trade.database.UserDatabase;

public class UserManager {

    private UserRepository userRepository;

    public UserManager(UserDatabase userDatabase){
        this.userRepository = new UserRepository(userDatabase);
    }

    public void userTest(){
        userRepository.insertTask();
    }
}

package com.phase2.trade.user;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {

    @PrimaryKey
    private Integer id;
    private String userName;
    private String email;
    private String telephone;
    private String password;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName  the username of this Person.
     * @param email     the email this Person.
     * @param telephone the telephone number of this person
     * @param password  the password this user set to
     */
    public User(String userName, String email, String telephone, String password) {
        this.userName = userName;
        this.email = email;
        this.telephone = telephone;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}


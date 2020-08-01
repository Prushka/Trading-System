package phase2.trade.user;

import phase2.trade.repository.UniqueId;

public class User implements UniqueId {

    private int uuid;
    private String userName;
    private String email;
    private String telephone;
    private String password;

    public Address address;

    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName  the username of this Person.
     * @param email     the email this Person.
     * @param password  the password this user set to
     */
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
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

    @Override
    public void setUid(int value) {
        this.uuid = value;
    }

    @Override
    public int getUid() {
        return uuid;
    }
}


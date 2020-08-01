package phase2.trade.user;

public class Guest extends User {
    /**
     * Creates a new User with userName, email, telephone and given password.
     *
     * @param userName  the username of this Person.
     * @param email     the email this Person.
     * @param password  the password this user set to
     */
    public Guest(String userName, String email,String password) {
        super(userName, email, password);
    }
}

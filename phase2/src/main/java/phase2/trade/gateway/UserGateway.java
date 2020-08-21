package phase2.trade.gateway;

import phase2.trade.user.User;

import java.util.List;

/**
 * The {@link User} gateway interface.
 *
 * @author Dan Lyu
 * @see User
 */
public interface UserGateway extends EntityGateway<User, UserGateway> {

    /**
     * Find a user who matches the given username or email and password.
     *
     * @param usernameOrEmail the username or email
     * @param password        the password
     * @return the list
     */
    List<User> findMatches(String usernameOrEmail, String password);

    /**
     * Find users by email.
     *
     * @param email the email
     * @return the list of users
     */
    List<User> findByEmail(String email);

    /**
     * Find users by name.
     *
     * @param userName the user name
     * @return the list of users
     */
    List<User> findByUserName(String userName);
}

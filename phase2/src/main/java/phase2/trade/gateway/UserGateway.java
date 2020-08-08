package phase2.trade.gateway;

import phase2.trade.user.User;

import java.util.List;

public interface UserGateway extends EntityGateway<User> {
    List<User> findMatches(String usernameOrEmail, String password);

    List<User> findByEmail(String email);

    List<User> findByUserName(String userName);

    List<User> findByCity(String city);
}

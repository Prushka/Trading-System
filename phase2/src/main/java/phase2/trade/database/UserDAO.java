package phase2.trade.database;

import phase2.trade.gateway.UserGateway;
import phase2.trade.user.User;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@link User} data access object.
 *
 * @author Dan Lyu
 * @see User
 */
public class UserDAO extends DAO<User, UserGateway> implements UserGateway {

    /**
     * Constructs a new User dao.
     *
     * @param databaseResourceBundle the database resource bundle
     */
    public UserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(User.class, databaseResourceBundle);
    }

    @Override
    public List<User> findMatches(String usernameOrEmail, String password) {
        final List<User> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            Predicate restrictions = builder.or(
                    builder.and(
                            builder.like(root.get("name"), usernameOrEmail),
                            builder.equal(root.get("password"), password)),
                    builder.and(
                            builder.like(root.get("email"), usernameOrEmail),
                            builder.equal(root.get("password"), password))
            );
            query.select(root).where(restrictions);
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    public List<User> findByEmail(String email) {
        final List<User> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            query.select(root).where(builder.like(root.get("email"), email));
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    public List<User> findByUserName(String userName) {
        final List<User> result = new ArrayList<>();
        criteria((builder, query, root) -> {
            query.select(root).where(builder.like(root.get("name"), userName));
            executeCriteriaQuery(result, query);
        });
        return result;
    }

    @Override
    protected UserGateway getThis() {
        return this;
    }
}


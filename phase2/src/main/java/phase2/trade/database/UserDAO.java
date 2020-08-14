package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends DAO<User, UserGateway> implements UserGateway {

    public UserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(User.class, databaseResourceBundle);
    }

    @Override
    public List<User> findMatches(String usernameOrEmail, String password) {
        final List<User> result = new ArrayList<>();
        criteria((builder, criteria, root) -> {
            Predicate restrictions = builder.or(
                    builder.and(
                            builder.like(root.get("name"), usernameOrEmail),
                            builder.equal(root.get("password"), password)),
                    builder.and(
                            builder.like(root.get("email"), usernameOrEmail),
                            builder.equal(root.get("password"), password))
            );
            criteria.select(root).where(restrictions);
            executeCriteriaQuery(result, criteria);
        });
        return result;
    }

    @Override
    public List<User> findByEmail(String email) {
        final List<User> result = new ArrayList<>();
        criteria((builder, criteria, root) -> {
            criteria.select(root).where(builder.like(root.get("email"), email));
            executeCriteriaQuery(result, criteria);
        });
        return result;
    }

    @Override
    public List<User> findByUserName(String userName) {
        final List<User> result = new ArrayList<>();
        criteria((builder, criteria, root) -> {
            criteria.select(root).where(builder.like(root.get("name"), userName));
            executeCriteriaQuery(result, criteria);
        });
        return result;
    }

    @Override
    public List<User> findByCity(String city) {
        final List<User> result = new ArrayList<>();
        criteria((builder, criteria, root) -> {
            criteria.select(root).where(builder.equal(root.get("city"), city));
            executeCriteriaQuery(result, criteria);
        });
        return result;
    }

    @Override
    protected UserGateway getThis() {
        return this;
    }
}


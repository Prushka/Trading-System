package phase2.trade.database;

import org.hibernate.query.Query;
import phase2.trade.gateway.UserGateway;
import phase2.trade.item.Item;
import phase2.trade.user.User;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDAO extends DAO<User, UserGateway> implements UserGateway {

    public UserDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(User.class, databaseResourceBundle);
    }

    @Override
    public List<User> findMatches(String usernameOrEmail, String password) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);

        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        Query<User> query = getCurrentSession().createQuery(criteria);


        Predicate restrictions = builder.and(
                builder.like(root.get("name"), usernameOrEmail),
                builder.equal(root.get("password"), password)
        );

        criteria.select(root).where(restrictions);
        return query.list();
    }

    @Override
    public List<User> findByEmail(String email) {
        CriteriaBuilder builder = getCriteriaBuilder();
        CriteriaQuery<User> criteria = builder.createQuery(User.class);

        Root<User> root = criteria.from(User.class);
        criteria.select(root);
        Query<User> query = getCurrentSession().createQuery(criteria);

        Predicate restriction = builder.like(root.get("email"), email);

        criteria.select(root).where(restriction);
        return query.list();
    }

    @Override
    public List<User> findByUserName(String userName) {
        Query query = getCurrentSession().createQuery("from User where name = :userName");
        query.setParameter("userName", userName);
        return query.list();
    }

    @Override
    public List<User> findByCity(String city) {
        Query query = getCurrentSession().createQuery("from User where address.city = :city");
        query.setParameter("city", city);
        return query.list();
    }

    public List<User> findAllUser() {
        return super.findAll();
    }

    @Override
    protected UserGateway getThis() {
        return this;
    }
}


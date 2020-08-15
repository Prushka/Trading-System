package phase2.trade.database;

import phase2.trade.avatar.Avatar;
import phase2.trade.gateway.AvatarGateway;
import phase2.trade.gateway.UserGateway;
import phase2.trade.user.User;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class AvatarDAO extends DAO<Avatar, AvatarGateway> implements AvatarGateway {

    public AvatarDAO(DatabaseResourceBundle databaseResourceBundle) {
        super(Avatar.class, databaseResourceBundle);
    }

    @Override
    protected AvatarGateway getThis() {
        return this;
    }
}


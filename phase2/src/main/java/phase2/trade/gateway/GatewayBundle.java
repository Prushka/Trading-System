package phase2.trade.gateway;

import phase2.trade.user.User;
import phase2.trade.user.UserFactory;

public class GatewayBundle {

    EntityBundle entityBundle;

    ConfigBundle configBundle;

    public ConfigBundle getConfigBundle() {
        return configBundle;
    }

    public EntityBundle getEntityBundle() {
        return entityBundle;
    }

    public GatewayBundle(EntityBundle entityBundle, ConfigBundle configBundle) {
        this.entityBundle = entityBundle;
        this.configBundle = configBundle;
    }
}

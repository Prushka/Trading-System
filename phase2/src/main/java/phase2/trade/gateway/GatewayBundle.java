package phase2.trade.gateway;

import phase2.trade.config.ConfigBundle;
import phase2.trade.database.nosql.Redis;

public class GatewayBundle {

    EntityBundle entityBundle;

    ConfigBundle configBundle;

    Redis redis; // TODO: add interface later

    public ConfigBundle getConfigBundle() {
        return configBundle;
    }

    public EntityBundle getEntityBundle() {
        return entityBundle;
    }

    public Redis getRedis() {
        return redis;
    }

    public GatewayBundle(EntityBundle entityBundle, ConfigBundle configBundle) {
        this.entityBundle = entityBundle;
        this.configBundle = configBundle;
        this.redis = new Redis(configBundle.getRedisConfig());
    }
}

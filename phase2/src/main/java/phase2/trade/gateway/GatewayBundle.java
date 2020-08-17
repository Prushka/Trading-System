package phase2.trade.gateway;

import phase2.trade.config.ConfigBundle;

public class GatewayBundle {

    EntityGatewayBundle entityBundle;

    ConfigBundle configBundle;

    public ConfigBundle getConfigBundle() {
        return configBundle;
    }

    public EntityGatewayBundle getEntityBundle() {
        return entityBundle;
    }

    public GatewayBundle(EntityGatewayBundle entityBundle, ConfigBundle configBundle) {
        this.entityBundle = entityBundle;
        this.configBundle = configBundle;
    }
}

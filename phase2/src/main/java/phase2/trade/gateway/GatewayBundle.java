package phase2.trade.gateway;

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

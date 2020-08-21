package phase2.trade.gateway;

import phase2.trade.config.ConfigBundle;

/**
 * The Gateway bundle. It contains {@link ConfigBundle} and {@link EntityGatewayBundle}
 *
 * @author Dan Lyu
 */
public class GatewayBundle {

    /**
     * The Entity bundle.
     */
    EntityGatewayBundle entityBundle;

    /**
     * The Config bundle.
     */
    ConfigBundle configBundle;

    /**
     * Gets config bundle.
     *
     * @return the config bundle
     */
    public ConfigBundle getConfigBundle() {
        return configBundle;
    }

    /**
     * Gets entity bundle.
     *
     * @return the entity bundle
     */
    public EntityGatewayBundle getEntityBundle() {
        return entityBundle;
    }

    /**
     * Constructs a new Gateway bundle.
     *
     * @param entityBundle the entity bundle
     * @param configBundle the config bundle
     */
    public GatewayBundle(EntityGatewayBundle entityBundle, ConfigBundle configBundle) {
        this.entityBundle = entityBundle;
        this.configBundle = configBundle;
    }
}

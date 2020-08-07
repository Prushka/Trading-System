package phase2.trade.user;

import phase2.trade.gateway.GatewayBundle;

public class AdministrativeManager {

    private final GatewayBundle gatewayBundle;

    private final PersonalUser operator;

    public AdministrativeManager(GatewayBundle gatewayBundle, PersonalUser operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
    }



}

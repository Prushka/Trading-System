package phase2.trade.user;

import phase2.trade.gateway.GatewayBundle;

public class AdministrativeManager {

    private final GatewayBundle gatewayBundle;

    private final RegularUser operator;

    public AdministrativeManager(GatewayBundle gatewayBundle, RegularUser operator) {
        this.gatewayBundle = gatewayBundle;
        this.operator = operator;
    }



}

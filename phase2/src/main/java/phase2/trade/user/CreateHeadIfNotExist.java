package phase2.trade.user;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.command.CreateUser;

public class CreateHeadIfNotExist {


    public CreateHeadIfNotExist(GatewayBundle gatewayBundle) {
        CreateUser createUser = new CreateUser(gatewayBundle, new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).configureSystemUser());
        createUser.execute((result, resultStatus) -> {

        }, "admin", "admin@example.com", "admin???", "country", "city", "HEAD_ADMIN");
    }
}

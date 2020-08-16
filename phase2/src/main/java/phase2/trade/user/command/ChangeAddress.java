package phase2.trade.user.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangeAddress extends UserCommand<User> {

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Address address = new Address();
            address.setCountry(argRequired(0,args));
            address.setTerritory(argRequired(1,args));
            address.setCity(argRequired(2,args));
            address.setFirstAddressLine(argRequired(3,args));
            address.setSecondAddressLine(argRequired(4,args));
            address.setPostalCode(argRequired(5,args));
            operator.getAddressBook().setSelectedAddress(address);
            gateway.merge(operator);
            save();
            callback.call(operator, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            // gateway.delete(userId);
        });
    }
}

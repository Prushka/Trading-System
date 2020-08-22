package phase2.trade.user.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.user.User;

import javax.persistence.Entity;

/**
 * The Change address.
 *
 * @author Dan Lyu
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {})
public class ChangeAddress extends UserCommand<User> {

    private Address oldAddress;

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            Address address = new Address();
            address.setCountry(argRequired(0, args));
            address.setTerritory(argRequired(1, args));
            address.setCity(argRequired(2, args));
            address.setFirstAddressLine(argRequired(3, args));
            address.setSecondAddressLine(argRequired(4, args));
            address.setPostalCode(argRequired(5, args));
            oldAddress = operator.getAddressBook().getSelectedAddress();
            operator.getAddressBook().setSelectedAddress(address);
            gateway.merge(operator);
            addEffectedEntity(Address.class, address.getId());
            save();
            callback.call(operator, new StatusSucceeded());
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            User user = gateway.findById(operator.getUid());
            user.getAddressBook().setSelectedAddress(oldAddress);
            gateway.update(user);
            updateUndo();
        });
    }

    private Address getOldAddress() {
        return oldAddress;
    }

    private void setOldAddress(Address oldAddress) {
        this.oldAddress = oldAddress;
    }
}

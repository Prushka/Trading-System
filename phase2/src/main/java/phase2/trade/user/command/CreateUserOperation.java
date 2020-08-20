package phase2.trade.user.command;

import phase2.trade.address.Address;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;
import phase2.trade.user.UserFactory;
import phase2.trade.validator.Validator;
import phase2.trade.validator.ValidatorFactory;
import phase2.trade.validator.ValidatorType;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false,
        persistent = true, permissionSet = {Permission.CreateUser})
public class CreateUserOperation extends UserCommand<User> {

    @Override
    public void execute(ResultStatusCallback<User> callback, String... args) { // username, email, password, permission_group, country, province, city,
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            List<User> usersByName = gateway.findByUserName(args[0]);
            List<User> usersByEmail = gateway.findByEmail(args[1]);
            if (usersByEmail.size() == 0 && usersByName.size() == 0) {
                String userName = argRequired(0, args);
                String email = argRequired(1, args);
                String password = argRequired(2, args);
                String permission_group = argRequired(3, args);
                String country = argRequired(4, args);
                String province = argRequired(5, args);
                String city = argRequired(6, args);
                Validator validator = new ValidatorFactory().getStringValidator(ValidatorType.NOT_EMPTY);
                if (!validator.validate(country) || !validator.validate(province) || !validator.validate(city)) {
                    callback.call(null, new StatusFailed("address.not.set"));
                    return;
                }
                User user =
                        new UserFactory(gatewayBundle.getConfigBundle().getPermissionConfig()).createByPermissionGroup(
                                userName, email, password, permission_group);
                Address address = new Address();
                address.setCountry(country);
                address.setTerritory(province);
                address.setCity(city);
                user.getAddressBook().setSelectedAddress(address);
                gateway.add(user);
                addEffectedEntity(User.class, user.getUid());
                save();
                callback.call(user, new StatusSucceeded());
            } else {
                callback.call(null, new StatusExist("user.name.email.exists"));
            }
        });
    }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction(gateway -> {
            gateway.delete(getOneEntity(User.class));
            updateUndo();
        });
    }
}

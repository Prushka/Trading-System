package phase2.trade.editor;

import phase2.trade.address.Address;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.config.ConfigBundle;
import phase2.trade.permission.PermissionGroup;
import phase2.trade.user.AccountState;
import phase2.trade.user.User;

import java.util.List;

/**
 * The {@link User} editor.
 * Used in classes like {@link phase2.trade.user.controller.UserTableController} to edit User in a TableView by administrative users.<p>
 *
 * @author Dan Lyu
 * @see User
 */
public class UserEditor extends Editor<User> {

    /**
     * Constructs a new User editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     */
    public UserEditor(List<User> entities, ConfigBundle configBundle) {
        super(entities, configBundle);
    }

    /**
     * Constructs a new User editor.
     *
     * @param entity       the entity
     * @param configBundle the config bundle
     */
    public UserEditor(User entity, ConfigBundle configBundle) {
        super(entity, configBundle);
    }

    /**
     * Alter name.
     *
     * @param name           the name
     * @param statusCallback the status callback
     */
    public void alterName(String name, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setName(name));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter email.
     *
     * @param email          the email
     * @param statusCallback the status callback
     */
    public void alterEmail(String email, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setEmail(email));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter password.
     *
     * @param password       the password
     * @param statusCallback the status callback
     */
    public void alterPassword(String password, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setReputation(Integer.parseInt(password)));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter account state.
     *
     * @param accountState   the account state
     * @param statusCallback the status callback
     */
    public void alterAccountState(String accountState, StatusCallback statusCallback) {
        entities.forEach(entity -> entity.setAccountState(AccountState.valueOf(accountState)));
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Alter reputation.
     *
     * @param reputation     the reputation
     * @param statusCallback the status callback
     */
    public void alterReputation(String reputation, StatusCallback statusCallback) {
        if (!isInt(reputation)) {
            statusCallback.call(new StatusFailed());
            return;
        }
        entities.forEach(entity -> entity.setReputation(Integer.parseInt(reputation)));
        statusCallback.call(new StatusSucceeded());
    }


    /**
     * Alter permission group.
     *
     * @param s              the s
     * @param statusCallback the status callback
     */
    public void alterPermissionGroup(String s, StatusCallback statusCallback) {
        PermissionGroup permissionGroup = PermissionGroup.valueOf(s);
        entities.forEach(entity -> {
            entity.setPermissionGroup(permissionGroup);
            entity.setUserPermission(configBundle.getPermissionConfig().getDefaultPermissions().get(permissionGroup));
        });
        statusCallback.call(new StatusSucceeded());
    }

    /**
     * Add address.
     *
     * @param country           the country
     * @param territory         the territory
     * @param city              the city
     * @param firstAddressLine  the first address line
     * @param secondAddressLine the second address line
     * @param postalCode        the postal code
     */
    public void addAddress(String country, String territory, String city, String firstAddressLine, String secondAddressLine
            , String postalCode) {
        entities.forEach(entity -> {
            Address address = new Address();
            address.setCountry(country);
            address.setTerritory(territory);
            address.setCity(city);
            address.setFirstAddressLine(firstAddressLine);
            address.setSecondAddressLine(secondAddressLine);
            address.setPostalCode(postalCode);
            entity.getAddressBook().setSelectedAddress(address);
        });
    }

}

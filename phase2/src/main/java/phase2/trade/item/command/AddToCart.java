package phase2.trade.item.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusExist;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.item.CartItemWrapper;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.user.User;

import javax.persistence.Entity;

/**
 * The Command used to add an item from a marketplace to a {@link phase2.trade.user.RegularUser}'s {@link phase2.trade.itemlist.Cart} as a {@link phase2.trade.item.CartItemWrapper} with a custom quantity.
 *
 * @author Dan Lyu
 * @see phase2.trade.item.CartItemWrapper
 * @see phase2.trade.itemlist.Cart
 */
@Entity
@CommandProperty(crudType = CRUDType.UPDATE, undoable = true,
        persistent = true, permissionSet = {Permission.ManageWishList})
public class AddToCart extends ItemCommand<Item> {

    @Override
    public void execute(ResultStatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getUserGateway().submitTransaction((gateway) -> {
            // operator.getCart().getSetOfItems().forEach(e -> System.out.println(e.getName() + " | " + e.getUid()));
            // for (Long uid : getEntityIds(toUpdate)) {
            if (operator.getCart().containsUid(toUpdate.getUid())) {
                callback.call(null, new StatusExist("item.exist.in.cart"));
                return;
            }
            if (operator.getInventory().containsUid(toUpdate.getUid())) {
                callback.call(null, new StatusExist("this.is.your.item"));
                return;
            }
            // }
            CartItemWrapper cartItemWrapper =
                    operator.getCart().addItemWithQuantity(toUpdate, Integer.parseInt(argRequired(0, "1", args)));
            gateway.update(operator);
            addEffectedEntity(CartItemWrapper.class, cartItemWrapper.getUid());
            save();
            callback.call(null, new StatusSucceeded());
        });
    }


    // protected Set<Long> getEntityIds(Collection<Item> collection) {
    //     Set<Long> ids = new HashSet<>();
    //     collection.forEach(e -> ids.add(e.getUid()));
    //     return ids;
    // }

    @Override
    protected void undoUnchecked() {
        getEntityBundle().getUserGateway().submitTransaction((userGateway) -> {
            User user = userGateway.findById(getOperator().getUid());
            user.getCart().removeCartItemWrapperById(getOneEntity(CartItemWrapper.class));
            updateUndo();
        });
    }
}

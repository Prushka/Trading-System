package phase2.trade.trade.command;

import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.StatusFailed;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.trade.OrderState;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.UserOrderBundle;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@CommandProperty(crudType = CRUDType.CREATE, undoable = false, persistent = true)
public class CreateTradeCommand extends TradeCommand<Trade> {

    // TODO: For example, everyone's first transaction must be lending, or trading.
    @Override
    public void execute(ResultStatusCallback<Trade> callback, String... args) {
        if (!checkPermission(callback)) return;

        getEntityBundle().getTradeGateway().submitTransaction((tradeGateway) -> {
            toUpdate.setLocalDateTime(LocalDateTime.now());
            Set<TradeOrder> ordersToRemove = new HashSet<>();


            for (TradeOrder order : toUpdate.getOrders()) {
                order.setOrderState(OrderState.PENDING_CONFIRMATION);
                if (order.getRightBundle().getTradeItemHolder().size() == 0 && order.getLeftBundle().getTradeItemHolder().size() == 0) {
                    // This is a placeholder order to resolve conflicts in multi-way trade (when two users are involved in a trade but they don't have an order between each other).
                    // It will be removed at this step.
                    ordersToRemove.add(order);
                } else if (order.getDateAndTime() == null) {
                    callback.call(null, new StatusFailed("not.all.date.and.time.are.filled"));
                    return;
                } else if (order.getAddressTrade() == null) {
                    callback.call(null, new StatusFailed("not.all.addresses.are.filled"));
                    return;
                }

            }

            Collection<Trade> tradesPlacedByUserInAWeek = tradeGateway.findByUser(operator, LocalDateTime.now(), LocalDateTime.now().minusDays(7));

            if (tradesPlacedByUserInAWeek.size() >= getConfigBundle().getTradeConfig().getTradePerWeek()) {
                callback.call(null, new StatusFailed("trade.exceed.limit.per.week"));
                return;
            }

            Map<User, Integer> userToItemsToLend = new HashMap<>();


            toUpdate.getOrders().removeAll(ordersToRemove);

            if (toUpdate.getOrders().size() == 0) {
                callback.call(null, new StatusFailed("no.order.detected"));
                return;
            }

            tradeGateway.add(toUpdate);


            getEntityBundle().getUserGateway().submitTransaction((userGateway) -> {
                for (TradeOrder order : toUpdate.getOrders()) { // persist UserOrderBundle
                    UserOrderBundle rightBundle = order.getRightBundle();
                    UserOrderBundle leftBundle = order.getLeftBundle();

                    // remove items from their carts if exist
                    // items that are involved in a Trade but not to be traded to a user won't be removed
                    // for example if A has 1, 2, 3 and 4 in his/her cart.
                    // B owns 1, C owns 2, D owns 3, E owns 4
                    // A gets 1, 2, B gets 3, C gets 4, etc..
                    // only 1 and 2 will be removed from A's cart, 3 will be removed from B's cart if it is in his/her cart
                    order.getLeftUser().getCart().removeItemByUid(rightBundle.getTradeItemHolder().getItemsAsIds());
                    order.getRightUser().getCart().removeItemByUid(leftBundle.getTradeItemHolder().getItemsAsIds());
                    userGateway.merge(order.getLeftUser());
                    userGateway.merge(order.getRightUser());
                }
            }, false);

            if (callback != null)
                callback.call(toUpdate, new StatusSucceeded());

            save();

        });


    }

}

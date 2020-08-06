package phase2.trade.trade;

import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

/**
 * This strategy describes how temporary trades need to schedule a second meeting to trade back items
 * @author Grace Leung
 */
class TemporaryTradingStrategy implements Tradable{

    Trade currTrade;

    TemporaryTradingStrategy(Trade currTrade) {
        this.currTrade = currTrade;
    }

    /**
     * Confirm a trade will take place and opens the trade
     * @param editingUser The user ID of who wishes to confirm to this trade
     */
    @Override
    public Trade confirmTrade(User editingUser) {
        boolean canStart = true;

        if (currTrade.getTradeState().equals(TradeState.CANCELLED) || currTrade.getTradeState().equals(TradeState.DEALT)){
            return currTrade;
        }
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            if (user.getUser().equals(editingUser) && !user.getConfirmations()){
                user.setConfirmations(true);
            }
            if (!user.getConfirmations()){
                canStart = false;
            }
        }

        if (currTrade.getTradeState().equals(TradeState.IN_PROGRESS) && canStart){
            currTrade.setTradeState(TradeState.PENDING_TRADE);
            for (UserOrderBundle user: currTrade.getOrder().getTraders()){
                user.setConfirmations(false);
            }
        } else if (currTrade.getTradeState().equals(TradeState.PENDING_TRADE) && canStart){
            makeTrades();
            scheduleTradeBack();
        }
        return currTrade;
    }

    // Adjusts transaction, borrow, and lend counts
    private void makeTrades() {
        for (UserOrderBundle user: currTrade.getOrder().getTraders()) {
            PersonalUser currUser = (PersonalUser) user.getUser();
            List<Item> newCartList = currUser.getItemList(InventoryType.CART).getListOfItems();
            List<Item> newInventory = currUser.getItemList(InventoryType.INVENTORY).getListOfItems();
            for (Item item : user.getTradeItemHolder().getListOfItems()) {
                newCartList.remove(item);
                newInventory.add(item);
                // currItem.setIsAvailable(false);
                // User other = item.getOwner();
            }
        }
    }

    // Schedules a second trade meeting
    private void scheduleTradeBack() {
        // LocalDateTime newDateAndTime = currTrade.getDateAndTime().plusMonths(timeLimit);
        // createTrade(currTrade.getAllUsers(), currTrade.getAllItems(), newDateAndTime,
                // currTrade.getLocation(), currTrade.getUid());
    }
}

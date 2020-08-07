package phase2.trade.trade;

import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

/**
 * This strategy describes how permanent trades may be the first meeting for a temporary trade
 * and changing item ownership
 * @author Grace Leung
 */
class PermanentTradingStrategy implements Tradable{

    Trade currTrade;

    PermanentTradingStrategy(Trade currTrade){
        this.currTrade = currTrade;
    }

    @Override
    public Trade confirmTrade(User editingUser) {
        boolean canStart = true;

        if (currTrade.getTradeState().equals(TradeState.CANCELLED) || currTrade.getTradeState().equals(TradeState.CLOSED)){
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
            openTrade();
        } else if (currTrade.getTradeState().equals(TradeState.PENDING_TRADE) && canStart){
            makeTrades();
            currTrade.setTradeState(TradeState.CLOSED);
        }

        return currTrade;
    }

    // Opens this trade and closes previous meetings
    void openTrade(){
        currTrade.setTradeState(TradeState.PENDING_TRADE);
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            user.setConfirmations(false);
        }
    }

    // Adjusts transaction, borrow, lend counts, and item ownership
    private void makeTrades() {
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            PersonalUser currUser = (PersonalUser) user.getUser();
            List<Item> newCartList = currUser.getItemList(InventoryType.CART).getListOfItems();
            List<Item> newInventory = currUser.getItemList(InventoryType.INVENTORY).getListOfItems();
            // Update that they traded
            for (Item item: user.getTradeItemHolder().getListOfItems()){
                newCartList.remove(item);
                newInventory.add(item);
                // User other = item.getOwner();
            }
        }
    }
}

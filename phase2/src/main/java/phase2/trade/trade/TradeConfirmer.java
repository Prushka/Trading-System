package phase2.trade.trade;

import phase2.trade.inventory.InventoryType;
import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import java.util.List;

public class TradeConfirmer {
    Integer timeLimit;

    TradeConfirmer(int timeLimit){
        this.timeLimit = timeLimit;
    }

    public Trade confirmTrade(Trade currTrade, User editingUser){
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
            currTrade.setTradeState(TradeState.PENDING_TRADE);
            resetConfirms(currTrade);
        } else if (currTrade.getTradeState().equals(TradeState.PENDING_TRADE) && canStart){
            makeTrades(currTrade);
            if(currTrade.getIsPermanent()){
                currTrade.setTradeState(TradeState.CLOSED);
            } else {
                currTrade.setTradeState(TradeState.PENDING_TRADEBACK);
                currTrade.getOrder().setDateAndTime(currTrade.getOrder().getDateAndTime().plusMonths(timeLimit));
                resetConfirms(currTrade);
            }
        } else if (currTrade.getTradeState().equals(TradeState.PENDING_TRADEBACK) && canStart){
            currTrade.setTradeState(TradeState.CLOSED);
        }
        return currTrade;
    }

    private void resetConfirms(Trade currTrade){
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            user.setConfirmations(false);
        }
    }

    private void makeTrades(Trade currTrade) {
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

}

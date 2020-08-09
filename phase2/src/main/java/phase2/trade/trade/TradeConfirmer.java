package phase2.trade.trade;

import phase2.trade.inventory.Cart;
import phase2.trade.inventory.Inventory;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.util.ArrayList;
import java.util.List;

public class TradeConfirmer {
    Integer timeLimit;

    public TradeConfirmer(int timeLimit){
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
        List<Item> tradedItems = new ArrayList<>();
        for (UserOrderBundle user: currTrade.getOrder().getTraders()) {
            RegularUser currUser = (RegularUser) user.getUser();
            List<Item> newCartList = currUser.getItemList(ItemListType.CART).getListOfItems();
            List<Item> newInventoryList = currUser.getItemList(ItemListType.INVENTORY).getListOfItems();
            for (Item item : user.getTradeItemHolder().getListOfItems()) {
                newCartList.remove(item);
                if (tradedItems.contains(item)){
                    newInventoryList.remove(item);
                }
                if (currTrade.getIsPermanent()){
                    newInventoryList.add(item);
                    tradedItems.add(item);
                }
            }
            Cart newCart = new Cart();
            newCart.setListOfItems(newCartList);
            currUser.setCart(newCart);

            Inventory newInventory = new Inventory();
            newInventory.setListOfItems(newInventoryList);
            currUser.setInventory(newInventory);
        }
    }

}

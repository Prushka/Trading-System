package phase2.trade.trade;

/**
 * Where trade dates and locations are edited
 * @author Grace Leung
 */
public class TradeEditor {

    /*
    Integer editLimit;

    public TradeEditor(int editLimit){
        this.editLimit = editLimit;
    }

    public void edit(Trade currTrade, User editingUser, String... args) {
        // Only unconfirmed parties a part of this trade can edit and users automatically confirm to their edit
        for (UserOrderBundle user: currTrade.getOrder().getTraders()){
            if (user.getUser().getEmail().equals(editingUser.getEmail()) && !user.hasConfirmed() && user.getEdits() == editLimit){
                cancelOrder(currTrade);
            } else if (user.getUser().getEmail().equals(editingUser.getEmail()) && !user.hasConfirmed() && user.getEdits() < editLimit){
                if (args.length == 5){
                    LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                    currTrade.getOrder().setDateAndTime(dateTime);
                } else if (args.length == 2){
                    Address location = new Address(args[0], args[1]);
                    ((MeetUpOrder)currTrade.getOrder()).setLocation(location);
                }
                user.setEdits(user.getEdits() + 1);
                user.setConfirm(true);
            } else if (!user.getUser().equals(editingUser)){
                user.setConfirm(false); // Need to alter so if they aren't in the trade at all, this doesn't happen
            }
        }
    }

    private void cancelOrder(Trade currTrade) {
        currTrade.setTradeState(TradeState.CANCELLED);
    }*/
}

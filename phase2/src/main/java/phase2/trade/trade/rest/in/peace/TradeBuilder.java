package phase2.trade.trade.rest.in.peace;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class TradeBuilder {

    /*
    Trade newTrade;

    // For UserOrderBundle(s)
    List<UserOrderBundle> traders;
    UserOrderBundleBuilder uobb;

    // For Order(s)
    LocalDateTime dateAndTime;
    Address location;
    TradeOrder order;

    // For a Trade
    Boolean isPermanent;

    TradeBuilder() {
        uobb = new UserOrderBundleBuilder();
        traders = new ArrayList<>();
    }

    void buildUserOrderBundle(User user, Set<Item> items){
        uobb.buildUser(user);
        uobb.buildDesiredItems(items);
        UserOrderBundle newBundle = uobb.buildUserOrderBundle();
        traders.add(newBundle);
    }

    void buildDateTime(String year, String month, String day, String hour, String minute){
        this.dateAndTime = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month),
                Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
        OnlineOrder newOrder = new OnlineOrder();
        newOrder.setDateAndTime(dateAndTime);
        order = newOrder;
    }

    void buildLocation(String country, String city){
        this.location = new Address(country, city);
        MeetUpOrder newOrder = new MeetUpOrder();
        newOrder.setDateAndTime(dateAndTime);
        newOrder.setLocation(location);
        order = newOrder;
    }

    void buildIsPermanent(boolean isPermanent){ this.isPermanent = isPermanent; }

    Trade buildTrade() {
        newTrade = new Trade();
        order.setTraders(traders);
        newTrade.setOrder(order);
        newTrade.setIsPermanent(isPermanent);
        newTrade.setTradeState(TradeState.IN_PROGRESS);
        return newTrade;
    }
     */
}

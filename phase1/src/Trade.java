public class Trade{
    // Trading Details
    private int tradeID;
    private User user1;
    private User user2;
    private int user1Edits;
    private int user2Edits;
    private boolean user1Confirms;
    private boolean user2Confirms;

    // Item Details
    private Item item1;
    private Item item2;
    private boolean isPermanent;
    private boolean isClosed;

    // Meeting Details
    private String date; // formatted year-month-day
    private String time; // formatted hours: minutes
    private String location;

    // If either item1 or item2 is null then it is a one-way trade or else it is a two-way trade
    public Trade(int tradeID, User user1, User user2, Item item1, Item item2, boolean isPermanent, String date,
                 String time, String location){
        this.tradeID = tradeID;
        this.user1 = user1;
        this.user2 = user2;
        this.item1 = item1;
        this.item2 = item2;
        this.isPermanent = isPermanent;
        this.date = date;
        this.time = time;
        this.location = location;

        // Default Values
        this.user1Edits = 0;
        this.user2Edits = 0;
        this.user1Confirms = false;
        this.user2Confirms = false;
        this.isClosed = true;
    }

    // Getters
    public int getTradeID(){ return tradeID;}
    public User getUser1(){ return user1;}
    public User getUser2(){ return user2;}
    public int getUser1Edits(){ return user1Edits;}
    public int getUser2Edits(){ return user2Edits;}
    public boolean getUser1Confirms(){ return user1Confirms;}
    public boolean getUser2Confirms(){ return user2Confirms;}
    public Item getItem1(){ return item1;}
    public Item getItem2(){ return item2;}
    public boolean getIsPermanent(){ return isPermanent;}
    public boolean getIsClosed(){ return isClosed;}
    public String getDate(){ return date;}
    public String getTime(){ return time;}
    public String getLocation(){ return location;}

    // Setters -- Do we allow changing items or permanency of trade
    public void increaseUser1Edits(){ user1Edits++;}
    public void increaseUser2Edits(){ user2Edits++;}
    public void confirmUser1(){ user1Confirms = true;}
    public void confirmUser2(){ user2Confirms = true;}
    public void unconfirmUser1(){ user1Confirms = false;}
    public void unconfirmUser2(){ user2Confirms = false;}
    public void openTrade(){ isClosed = false;}
    public void closeTrade(){ isClosed = true;}
    public void setDate(String new_date){ date = new_date;}
    public void setTime(String new_time){ time = new_time;}
    public void setLocation(String new_location){ location = new_location;}

    /**
     * @return A description of this trade.
     */
    @Override
    public String toString() {
        if (item1 == null && item2 == null){
            return "This is an invalid trade.";
        } else if (item1 == null){
            return user1.getName() + " lends to " + user2.getName() + " at " + this.location + " on " + this.date + ", "
                    + this.time;
        } else if (item2 == null){
            return user1.getName() + " borrows from " + user2.getName() + " at " + this.location + " on " + this.date +
                    ", " + this.time;
        } else {
            return user1.getName() + " trades with " + user2.getName() + " at " + this.location + " on " + this.date +
                    ", " + this.time;
        }
    }
}
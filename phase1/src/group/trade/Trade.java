package group.trade;

import group.item.Item;
import group.repository.UniqueId;
import group.repository.map.EntityMappable;
import group.repository.map.MappableBase;
import group.user.PersonalUser;

import java.sql.Timestamp;
import java.util.List;

public class Trade extends MappableBase implements EntityMappable, UniqueId {

    // final will not work if the entity wants to use CSV Repository

    // it doesn't effect serializationRepository

    // Trading Details
    private int tradeID;
    private PersonalUser user1;
    private PersonalUser user2;
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
    private Timestamp dateAndTime;
    private String location;

    public Trade(List<String> record){
        super(record);
    }

    // If either item1 or item2 is null then it is a one-way trade or else it is a two-way trade
    public Trade(int tradeID, PersonalUser user1, PersonalUser user2, Item item1, Item item2, boolean isPermanent,
            Timestamp dateAndTime, String location){
        this.tradeID = tradeID;
        this.user1 = user1;
        this.user2 = user2;
        this.item1 = item1;
        this.item2 = item2;
        this.isPermanent = isPermanent;
        this.dateAndTime = dateAndTime;
        this.location = location;

        // Default Values
        this.user1Edits = 0;
        this.user2Edits = 0;
        this.user1Confirms = true;
        this.user2Confirms = false;
        this.isClosed = true;
    }

    // Getters
    public int getTradeID(){ return tradeID;}
    public PersonalUser getUser1(){ return user1;}
    public PersonalUser getUser2(){ return user2;}
    public int getUser1Edits(){ return user1Edits;}
    public int getUser2Edits(){ return user2Edits;}
    public boolean getUser1Confirms(){ return user1Confirms;}
    public boolean getUser2Confirms(){ return user2Confirms;}
    public Item getItem1(){ return item1;}
    public Item getItem2(){ return item2;}
    public boolean getIsPermanent(){ return isPermanent;}
    public boolean getIsClosed(){ return isClosed;}
    public Timestamp getDateAndTime(){ return dateAndTime;}
    public String getLocation(){ return location;}

    // Setters
    public void increaseUser1Edits(){ user1Edits++;}
    public void increaseUser2Edits(){ user2Edits++;}
    public void confirmUser1(){ user1Confirms = true;}
    public void confirmUser2(){ user2Confirms = true;}
    public void unconfirmUser1(){ user1Confirms = false;}
    public void unconfirmUser2(){ user2Confirms = false;}
    public void openTrade(){ isClosed = false;}
    public void closeTrade(){ isClosed = true;}
    public void setDateAndTime(Timestamp new_dateAndTime){ dateAndTime = new_dateAndTime;}
    public void setLocation(String new_location){ location = new_location;}

    /**
     * @return A description of this trade.
     */
    @Override
    public String toString() {
        if (item1 == null && item2 == null){
            return "This is an invalid trade.";
        } else if (item1 == null){
            return user1.getName() + " lends to " + user2.getName() + " at " + this.location + " on " +
                    this.dateAndTime;
        } else if (item2 == null){
            return user1.getName() + " borrows from " + user2.getName() + " at " + this.location + " on " +
                    this.dateAndTime;
        } else {
            return user1.getName() + " trades with " + user2.getName() + " at " + this.location + " on " +
                    this.dateAndTime;
        }
    }

    @Override
    public void setUid(long value) {
        // TODO
    }

    @Override
    public long getUid() {
        return 0;
    }
}
package group.trade;

import group.repository.UniqueId;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;

import java.util.Date;
import java.util.List;

public class Trade extends MappableBase implements CSVMappable, UniqueId {
    // Trading Details
    private Long tradeID;
    private Long user1;
    private Long user2;
    private Integer user1Edits;
    private Integer user2Edits;
    private Boolean user1Confirms;
    private Boolean user2Confirms;
    private Long prevMeeting;

    // Item Details
    private Long item1;
    private Long item2;
    private Boolean isPermanent;
    private Boolean isClosed;

    // Meeting Details
    private Date dateAndTime;
    private String location;

    // Needed to implement Mappable Base
    public Trade(List<String> record){
        super(record);
    }

    // If either item1 or item2 is null then it is a one-way trade or else it is a two-way trade
    public Trade(Long user1, Long user2, Long item1, Long item2, Boolean isPermanent, Date dateAndTime,
                 String location, Long prevMeeting){
        this.user1 =  user1;
        this.user2 =  user2;
        this.item1 =  item1;
        this.item2 =  item2;
        this.isPermanent = isPermanent;
        this.dateAndTime = dateAndTime;
        this.location =  location;
        this.prevMeeting = prevMeeting;

        // Default Values
        this.user1Edits = 0;
        this.user2Edits = 0;
        this.user1Confirms = true;
        this.user2Confirms = false;
        this.isClosed = true;
    }

    // Getters
    @Override
    public long getUid() { return this.tradeID;}
    public long getUser1(){ return user1;}
    public long getUser2(){ return user2;}
    public int getUser1Edits(){ return user1Edits;}
    public int getUser2Edits(){ return user2Edits;}
    public boolean getUser1Confirms(){ return user1Confirms;}
    public boolean getUser2Confirms(){ return user2Confirms;}
    public Long getItem1(){ return item1;}
    public Long getItem2(){ return item2;}
    public boolean getIsPermanent(){ return isPermanent;}
    public boolean getIsClosed(){ return isClosed;}
    public Date getDateAndTime(){ return dateAndTime;}
    public String getLocation(){ return location;}
    public Long getPrevMeeting(){ return prevMeeting;}

    // Setters
    @Override
    public void setUid(long new_uid) { this.tradeID = new_uid;}
    public void increaseUser1Edits(){ user1Edits++;}
    public void increaseUser2Edits(){ user2Edits++;}
    public void confirmUser1(){ user1Confirms = true;}
    public void confirmUser2(){ user2Confirms = true;}
    public void unconfirmUser1(){ user1Confirms = false;}
    public void unconfirmUser2(){ user2Confirms = false;}
    public void openTrade(){ isClosed = false;}
    public void closeTrade(){ isClosed = true;}
    public void setDateAndTime(Date newDateAndTime){ dateAndTime = newDateAndTime;}
    public void setLocation(String newLocation){ location = newLocation;}

    /**
     * @return A description of this trade.
     */
    @Override
    public String toString() {
        if (item1 == null && item2 == null){
            return "This is an invalid trade.";
        } else if (item1 == null){
            return "UserID: " + user1 + " lends to UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        } else if (item2 == null){
            return "UserID: " + user1 + " borrows from UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        } else {
            return "UserID: " + user1 + " trades with UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        }
    }
}
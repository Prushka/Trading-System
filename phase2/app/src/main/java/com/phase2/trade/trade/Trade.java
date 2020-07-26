package main.java.com.phase2.trade.trade;

import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;

import java.util.*;
import java.time.LocalDateTime;

class Trade extends MappableBase implements CSVMappable, UniqueId {
    // Trading Details
    private Integer tradeID;
    private Integer user1;
    private Integer user2;
    private Integer user1Edits;
    private Integer user2Edits;
    private Boolean user1Confirms;
    private Boolean user2Confirms;
    private Integer prevMeeting;

    // Item Details
    private Integer item1;
    private Integer item2;
    private Boolean isPermanent;
    private Boolean isClosed;

    // Meeting Details
    private LocalDateTime dateAndTime;
    private String location;

    /**
     * @param record Representation of a trade's parameters
     */
    // Needed to implement Mappable Base
    public Trade(List<String> record){
        super(record);
    }

    /**
     * Describes a one-way or two-way trade
     * @param user1 The user ID of the trade initiator
     * @param user2 The user ID of the trade respondent
     * @param item1 The item ID of the item that the initiator wishes to lend
     * @param item2 The item ID of the item that the initiator wishes to borrow
     * @param isPermanent True iff this trade is permanent
     * @param dateAndTime When this trade occurs
     * @param location Where this trade occurs
     * @param prevMeeting The previous trade meeting
     */
    public Trade(Integer user1, Integer user2, Integer item1, Integer item2, Boolean isPermanent, LocalDateTime dateAndTime,
                 String location, Integer prevMeeting){
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

    // GETTERS

    /**
     * @return The trade ID of this trade
     */
    @Override
    public int getUid() { return this.tradeID;}

    /**
     * @return The user ID of the initiator
     */
    public int getUser1(){ return user1;}

    /**
     * @return The user ID of the respondent
     */
    public int getUser2(){ return user2;}

    /**
     * @return The amount of times the initiator has edited
     */
    public int getUser1Edits(){ return user1Edits;}

    /**
     * @return The amount of times the respondent has edited
     */
    public int getUser2Edits(){ return user2Edits;}

    /**
     * @return True iff the initiator has confirmed to the opening/ completion of this trade
     */
    public boolean getUser1Confirms(){ return user1Confirms;}

    /**
     * @return True iff the respondent has confirmed to the opening/ completion of this trade
     */
    public boolean getUser2Confirms(){ return user2Confirms;}

    /**
     * @return The item ID of what the initiator wants to lend to the respondent
     */
    public Integer getItem1(){ return item1;}

    /**
     * @return The item ID of what the initiator wants to borrow from the respondent
     */
    public Integer getItem2(){ return item2;}

    /**
     * @return True iff this trade is permanent
     */
    public boolean getIsPermanent(){ return isPermanent;}

    /**
     * @return True iff this trade is closed
     */
    public boolean getIsClosed(){ return isClosed;}

    /**
     * @return The date and time of this trade
     */
    public LocalDateTime getDateAndTime(){ return dateAndTime;}

    /**
     * @return The location of this trade
     */
    public String getLocation(){ return location;}

    /**
     * @return The trade ID of the previous trade if applicable
     */
    public Integer getPrevMeeting(){ return prevMeeting;}

    // SETTERS

    /**
     * @param new_uid The new trade ID for this trade
     */
    @Override
    public void setUid(int new_uid) { this.tradeID = new_uid;}

    /**
     * Increases the initiator's edits by one.
     */
    public void increaseUser1Edits(){ user1Edits++;}

    /**
     * Increase the initiator's edits by one.
     */
    public void increaseUser2Edits(){ user2Edits++;}

    /**
     * Confirms the initiator's commitment to the trade/ verification of completion
     */
    public void confirmUser1(){ user1Confirms = true;}

    /**
     * Confirms the respondent's commitment to the trade/ verification of completion
     */
    public void confirmUser2(){ user2Confirms = true;}

    /**
     * Un-confirms the initiator's commitment to the trade/ verification of completion
     */
    public void unconfirmUser1(){ user1Confirms = false;}

    /**
     * Un-confirms the respondent's commitment to the trade/ verification of completion
     */
    public void unconfirmUser2(){ user2Confirms = false;}

    /**
     * Sets the state of this trade to open
     */
    public void openTrade(){ isClosed = false;}

    /**
     * Sets the state of this trade to close
     */
    public void closeTrade(){ isClosed = true;}

    /**
     * Sets a trade to a new date and time
     * @param newDateAndTime The new date and time of this trade
     */
    public void setDateAndTime(LocalDateTime newDateAndTime){ dateAndTime = newDateAndTime;}

    /**
     * Sets a trade to a new location
     * @param newLocation The new location of this trade
     */
    public void setLocation(String newLocation){ location = newLocation;}

    /**
     * @return A description of this trade.
     */
    @Override
    public String toString() {
        if (item1 == null && item2 == null){
            return "This is an empty trade, nothing is traded between UserID: " + user1 + " and UserID: " + user2;
        } else if (item1 == null){
            return "UserID: " + user1 + " borrows from UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        } else if (item2 == null){
            return "UserID: " + user1 + " lends to UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        } else {
            return "UserID: " + user1 + " trades with UserID: " + user2 + " at " + this.location + " on " +
                    this.dateAndTime;
        }
    }
    
}
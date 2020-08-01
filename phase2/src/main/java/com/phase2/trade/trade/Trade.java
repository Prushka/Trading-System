package com.phase2.trade.trade;

import main.java.com.phase2.trade.repository.*;
import main.java.com.phase2.trade.repository.reflection.*;

import java.util.*;
import java.time.LocalDateTime;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
class Trade {
    // Trading Details
    private List<Integer> users;
    private List<Boolean> confirmations;
    private List<Integer> edits;
    private List<List<Integer>> items;

    // Meeting Details
    private Integer tradeID;
    private LocalDateTime dateAndTime;
    private String location;
    private Boolean isClosed;

    /**
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     */
    Trade(List<Integer> users, List<List<Integer>> items, LocalDateTime dateAndTime, String
            location){
        this.users = users;
        this.items = items;
        this.dateAndTime = dateAndTime;
        this.location =  location;

        // Default Values
        edits = new ArrayList<>(users.size());
        confirmations = new ArrayList<>(users.size());
        confirmations.set(0, true);
        isClosed = true;
    }

    // GETTERS

    /**
     * @return The trade ID of this trade
     */
    @Override
    public int getUid() { return this.tradeID;}

    /**
     * @return List of all userIDs participating in this trade
     */
    List<Integer> getAllUsers() { return this.users;}

    /**
     * @return List of all userIDs participating in this trade
     */
    List<Boolean> getAllConfirmations() { return this.confirmations;}

    /**
     * @return The list of desired items for the user
     */
    List<List<Integer>> getAllItems(){ return this.items;}

    /**
     * @return The amount of times the user has edited
     */
    int getUserEdits(int userID){ return this.edits.get(this.users.indexOf(userID));}

    /**
     * @return True iff the user has confirmed to the opening/ completion of this trade
     */
    boolean getUserConfirms(int userID){ return this.confirmations.get(this.users.indexOf(userID));}

    /**
     * @return The date and time of this trade
     */
    LocalDateTime getDateAndTime(){ return dateAndTime;}

    /**
     * @return The location of this trade
     */
    String getLocation(){ return location;}

    /**
     * @return True iff this trade is closed
     */
    boolean getIsClosed(){ return isClosed;}

    // SETTERS

    /**
     * @param new_uid The new trade ID for this trade
     */
    @Override
    public void setUid(int new_uid) { this.tradeID = new_uid;}

    /**
     * Increases the user's edits by one.
     */
    void increaseUserEdits(int userID){ this.edits.set(this.users.indexOf(userID),
            this.edits.get(this.users.indexOf(userID)) + 1);}

    /**
     * Confirms the user's commitment to the trade/ verification of completion
     */
    void confirmUser(int userID){ this.confirmations.set(this.users.indexOf(userID), true);}

    /**
     * Un-confirms the user's commitment to the trade/ verification of completion
     */
    void unconfirmUser(int userID){ this.confirmations.set(this.users.indexOf(userID), false);}

    /**
     * Sets a trade to a new date and time
     * @param newDateAndTime The new date and time of this trade
     */
    void setDateAndTime(LocalDateTime newDateAndTime){ dateAndTime = newDateAndTime;}

    /**
     * Sets a trade to a new location
     * @param newLocation The new location of this trade
     */
    void setLocation(String newLocation){ location = newLocation;}

    /**
     * Sets the state of this trade to open
     */
    void openTrade(){ isClosed = false;}

    /**
     * Sets the state of this trade to close
     */
    void closeTrade(){ isClosed = true;}
}
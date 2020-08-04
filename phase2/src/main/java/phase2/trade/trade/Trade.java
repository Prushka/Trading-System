package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.Address;
import phase2.trade.user.PersonalUser;

import java.util.*;
import java.time.LocalDateTime;
import javax.persistence.*;

/**
 * Represents a trade between users at a specific date and place
 * @author Grace Leung
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToMany
    private List<PersonalUser> users;
    private List<Boolean> confirmations;
    private List<Integer> edits;

    @OneToMany
    private List<List<Item>> items;
    private Long prevMeeting;

    private LocalDateTime dateAndTime;
    @Embedded
    @Column(name="TRADE_LOCATION")
    private Address location;
    private Boolean isClosed;

    public Trade() {}

    /**
     * @param users All the userID's associated with this trade
     * @param items All the items associated with this trade. Each list corresponds to the desired
     *              items of the userID in users with the same index
     * @param dateAndTime When this trade takes place
     * @param location Where this trade takes place
     * @param prevMeeting The trade ID of the previous meeting
     */
    Trade(List<PersonalUser> users, List<List<Item>> items, LocalDateTime dateAndTime, Address
            location, Long prevMeeting){
        this.users = users;
        this.items = items;
        this.dateAndTime = dateAndTime;
        this.location =  location;
        this.prevMeeting = prevMeeting;

        // Default Values
        edits = new ArrayList<>(users.size());
        confirmations = new ArrayList<>(users.size());
        confirmations.set(0, true);
        isClosed = true;
    }

    // GETTERS

    /**
     * @return List of all userIDs participating in this trade
     */
    List<PersonalUser> getAllUsers() { return this.users;}

    /**
     * @return The list of desired items for the user
     */
    List<List<Item>> getAllItems(){ return this.items;}

    /**
     * @return The amount of times the user has edited
     */
    int getUserEdits(int userID){ return this.edits.get(this.users.indexOf(userID));}

    /**
     * @return True iff the user has confirmed to the opening/ completion of this trade
     */
    boolean getUserConfirms(int userID){ return this.confirmations.get(this.users.indexOf(userID));}

    /**
     * @return True iff all the users have confirmed to the opening/ completion of this trade
     */
    boolean getAllConfirmed(){
        for (Boolean i: confirmations){
            if (i.equals(false)){
                return false;
            }
        }
        return true;
    }

    /**
     * @return The date and time of this trade
     */
    LocalDateTime getDateAndTime(){ return dateAndTime;}

    /**
     * @return The location of this trade
     */
    Address getLocation(){ return location;}

    /**
     * @return The trade ID of the previous trade if applicable
     */
    Long getPrevMeeting(){ return prevMeeting;}

    /**
     * @return True iff this trade is closed
     */
    boolean getIsClosed(){ return isClosed;}

    Long getUid() {
        return uid;
    }

    // SETTERS

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
     * Un-confirms all user's commitment to the trade/ verification of completion
     */
    void unconfirmAll(){
        confirmations = new ArrayList<>(confirmations.size());
    }

    /**
     * Sets a trade to a new date and time
     * @param newDateAndTime The new date and time of this trade
     */
    void setDateAndTime(LocalDateTime newDateAndTime){ dateAndTime = newDateAndTime;}

    /**
     * Sets a trade to a new location
     * @param newLocation The new location of this trade
     */
    void setLocation(Address newLocation){ location = newLocation;}

    /**
     * Sets the state of this trade to open
     */
    void openTrade(){ isClosed = false;}

    /**
     * Sets the state of this trade to close
     */
    void closeTrade(){ isClosed = true;}

    void setUid(Long uid) {
        this.uid = uid;
    }

    abstract Tradable getStrategy();
    abstract void setStrategy(Tradable newStrategy);
}
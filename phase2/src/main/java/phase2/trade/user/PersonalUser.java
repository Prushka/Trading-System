package phase2.trade.user;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PersonalUser extends User {

    private Boolean isFrozen;
    private Integer lendCount;
    private Integer borrowCount;
    private Integer numTransactions;
    private Boolean requestToUnfreeze;
    //private int incompleteTrades;

    /**
     * Creates a PersonalUser with the given userName, email, telephone, password
     * and initializes all other instance variables.
     *
     * @param userName  username of this user
     * @param email     email of this user
     * @param password  password of this user
     */
    public PersonalUser(String userName, String email, String password) {
        super(userName, email, password);
        isFrozen = false;
        lendCount = 0;
        borrowCount = 0;
        numTransactions = 0;
        requestToUnfreeze = false;
        //incompleteTrades = 0;
    }

    public PersonalUser() {

    }

    //public int getIncompleteTrades() {
        //return incompleteTrades;
    //}

    public boolean getIsFrozen() {
        return isFrozen;
    }

    public void setIsFrozen(boolean isFrozen) {
        this.isFrozen = isFrozen;
    }

    public int getLendCount() {
        return lendCount;
    }

    public void setLendCount(int lendCount) {
        this.lendCount = lendCount;
    }

    public int getBorrowCount() {
        return borrowCount;
    }

    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    public boolean getShouldBeFreezedUser() {
        return lendCount < borrowCount;
    }

    public int getNumTransactions() {
        return numTransactions;
    }

    public void setNumTransactions(int numTransactions) {
        this.numTransactions = numTransactions;
    }

    public void setRequestToUnfreeze(boolean state) {
        requestToUnfreeze = state;
    }

    public boolean getRequestToUnfreeze() {
        return requestToUnfreeze;
    }


    /*
     * returns the user IDs of the top three most frequent traders for this user as a map.
     * if this user has not traded with three different users, returns top 2 or the top
     * trader accordingly.
     * @return map of the top 3 most frequent traders for this user
  public void setTraderFrequency(Integer userID) {
        if (traderFrequency.containsKey(userID)){
            traderFrequency.put(userID, traderFrequency.get(userID) + 1);
        } else {
            traderFrequency.put(userID, 1);
        }
    }
    public Map<Integer, Integer> getTopThreeTraders() {
        int len = traderFrequency.keySet().toArray().length;
        if (traderFrequency.isEmpty()) {
            return null;
        } else if (len <= 3) {
            return traderFrequency;
        } else {
            Map<Integer, Integer> ans = new HashMap<>();
            List<Integer> v = (ArrayList<Integer>) traderFrequency.values();
            Collections.sort(v);
            v = v.subList(v.size() - 3, v.size());
            for (int i = 0; i < 3; i++) {
                ans.put(keyFromValue(v.get(i)), v.get(i));
            }
            return ans;
        }
    }

    /*
     * helper method that returns a key from a value only for a one-to-one Map<Integer, Integer></>
     * type map.
     * @param value times traded with someone else
     * @return key mapped to the given value

   private Long keyFromValue(Integer value) {
        for (Map.Entry<Integer , Integer> entry : traderFrequency.entrySet()) {
            if (value.equals(entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }*/
}

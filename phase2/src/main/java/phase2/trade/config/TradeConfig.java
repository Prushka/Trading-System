package phase2.trade.config;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The Trade config.
 *
 * @author Dan Lyu
 * @author Grace Leung
 */
public class TradeConfig {

    private final IntegerProperty editLimit = new SimpleIntegerProperty(3);

    private final IntegerProperty frozenIncomplete = new SimpleIntegerProperty(3);

    private final IntegerProperty tradePerWeek = new SimpleIntegerProperty(3);

    private final IntegerProperty timeLimit = new SimpleIntegerProperty(3);

    /**
     * Gets edit limit.
     *
     * @return the edit limit
     */
    public int getEditLimit() {
        return editLimit.get();
    }

    /**
     * Edit limit property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty editLimitProperty() {
        return editLimit;
    }

    /**
     * Sets edit limit.
     *
     * @param editLimit the edit limit
     */
    public void setEditLimit(int editLimit) {
        this.editLimit.set(editLimit);
    }

    /**
     * Gets frozen incomplete.
     *
     * @return the frozen incomplete
     */
    public int getFrozenIncomplete() {
        return frozenIncomplete.get();
    }

    /**
     * Frozen incomplete property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty frozenIncompleteProperty() {
        return frozenIncomplete;
    }

    /**
     * Sets frozen incomplete.
     *
     * @param frozenIncomplete the frozen incomplete
     */
    public void setFrozenIncomplete(int frozenIncomplete) {
        this.frozenIncomplete.set(frozenIncomplete);
    }

    /**
     * Gets trade per week.
     *
     * @return the trade per week
     */
    public int getTradePerWeek() {
        return tradePerWeek.get();
    }

    /**
     * Trade per week property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty tradePerWeekProperty() {
        return tradePerWeek;
    }

    /**
     * Sets trade per week.
     *
     * @param tradePerWeek the trade per week
     */
    public void setTradePerWeek(int tradePerWeek) {
        this.tradePerWeek.set(tradePerWeek);
    }

    /**
     * Gets time limit.
     *
     * @return the time limit
     */
    public int getTimeLimit() {
        return timeLimit.get();
    }

    /**
     * Time limit property integer property.
     *
     * @return the integer property
     */
    public IntegerProperty timeLimitProperty() {
        return timeLimit;
    }

    /**
     * Sets time limit.
     *
     * @param timeLimit the time limit
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit.set(timeLimit);
    }
}

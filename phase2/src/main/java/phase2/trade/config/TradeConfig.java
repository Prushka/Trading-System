package phase2.trade.config;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class TradeConfig {

    private IntegerProperty editLimit = new SimpleIntegerProperty(3);

    private IntegerProperty frozenIncomplete = new SimpleIntegerProperty(3);

    private IntegerProperty tradePerWeek = new SimpleIntegerProperty(3);

    private IntegerProperty timeLimit = new SimpleIntegerProperty(3);

    public int getEditLimit() {
        return editLimit.get();
    }

    public IntegerProperty editLimitProperty() {
        return editLimit;
    }

    public void setEditLimit(int editLimit) {
        this.editLimit.set(editLimit);
    }

    public int getFrozenIncomplete() {
        return frozenIncomplete.get();
    }

    public IntegerProperty frozenIncompleteProperty() {
        return frozenIncomplete;
    }

    public void setFrozenIncomplete(int frozenIncomplete) {
        this.frozenIncomplete.set(frozenIncomplete);
    }

    public int getTradePerWeek() {
        return tradePerWeek.get();
    }

    public IntegerProperty tradePerWeekProperty() {
        return tradePerWeek;
    }

    public void setTradePerWeek(int tradePerWeek) {
        this.tradePerWeek.set(tradePerWeek);
    }

    public int getTimeLimit() {
        return timeLimit.get();
    }

    public IntegerProperty timeLimitProperty() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit.set(timeLimit);
    }
}

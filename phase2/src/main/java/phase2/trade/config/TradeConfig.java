package phase2.trade.config;

public class TradeConfig {

    private int editLimit = 3;

    private int frozenIncomplete = 3;

    private int tradePerWeek = 3;

    private int timeLimit = 3;

    public int getTradePerWeek() {
        return tradePerWeek;
    }

    public int getEditLimit() {
        return editLimit;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setEditLimit(int editLimit) {
        this.editLimit = editLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
}

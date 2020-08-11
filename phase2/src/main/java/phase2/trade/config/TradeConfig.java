package phase2.trade.config;

public class TradeConfig implements ConfigDefaultable {

    private int editLimit;

    private int timeLimit;

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

    @Override
    public void initDefault() {

    }
}

package phase2.trade.user;

public enum AccountState {

    NORMAL("normal.state"),
    ON_vacation("on.vacation.state");

    public String language;

    AccountState(String language) {
        this.language = language;
    }


    public static AccountState getByLanguage(String language) {
        for (AccountState e : values()) {
            if (e.language.equals(language)) return e;
        }
        return NORMAL;
    }
}

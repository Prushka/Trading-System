package phase2.trade.user;

/**
 * The Account state.
 *
 * @author Dan Lyu
 */
public enum AccountState {

    /**
     * Normal account state.
     */
    NORMAL("normal.state"),
    /**
     * On vacation account state.
     */
    ON_VACATION("on.vacation.state");

    /**
     * The Language.
     */
    public String language;

    AccountState(String language) {
        this.language = language;
    }


    /**
     * Gets by language.
     *
     * @param language the language
     * @return the by language
     */
    public static AccountState getByLanguage(String language) {
        for (AccountState e : values()) {
            if (e.language.equals(language)) return e;
        }
        return NORMAL;
    }
}

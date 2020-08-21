package phase2.trade.item;

/**
 * The enum Willingness.
 *
 * @author Dan Lyu
 */
public enum Willingness {
    /**
     * Lend willingness.
     */
    LEND("lend.willingness"),
    /**
     * Sell willingness.
     */
    SELL("sell.willingness"),
    /**
     * Private willingness.
     */
    Private("private.willingness");

    /**
     * The Language.
     */
    public String language;

    Willingness(String language) {
        this.language = language;
    }

    /**
     * Gets by language.
     *
     * @param language the language
     * @return the by language
     */
    public static Willingness getByLanguage(String language) {
        for (Willingness e : values()) {
            if (e.language.equals(language)) return e;
        }
        return Private;
    }
}

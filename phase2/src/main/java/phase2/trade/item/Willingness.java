package phase2.trade.item;

public enum Willingness {
    LEND("lend.willingness"), SELL("sell.willingness"), Private("private.willingness");

    public String language;

    Willingness(String language) {
        this.language = language;
    }

    public static Willingness getByLanguage(String language) {
        for (Willingness e : values()) {
            if (e.language.equals(language)) return e;
        }
        return Private;
    }
}

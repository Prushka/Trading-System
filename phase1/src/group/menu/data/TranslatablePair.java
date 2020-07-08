package group.menu.data;

/**
 * A pair of translatable String and parameters to be formatted
 *
 * @author Dan Lyu
 */
public class TranslatablePair {

    /**
     * The String identifier
     */
    private final String translatable;

    /**
     * The parameters that will be taken in when formatting the {@link #translatable}
     */
    private final Object[] paras;

    /**
     * @param translatable The String identifier
     * @param paras The parameters that will be taken in when formatting the {@link #translatable}
     */
    public TranslatablePair(String translatable, Object[] paras){
        this.translatable = translatable;
        this.paras = paras;
    }

    /**
     * @return {@link #translatable}
     */
    public String getTranslatable() {
        return translatable;
    }

    /**
     * @return {@link #paras}
     */
    public Object[] getParas() {
        return paras;
    }

}

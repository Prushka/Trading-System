package group.menu.data;

/**
 * A pair of translatable identifier and parameters to be formatted
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
     * @param translatable the String identifier
     * @param paras        parameters that will be taken in when formatting the {@link #translatable}
     */
    public TranslatablePair(String translatable, Object[] paras) {
        this.translatable = translatable;
        this.paras = paras;
    }

    /**
     * @return the translatable identifier
     */
    public String getTranslatable() {
        return translatable;
    }

    /**
     * @return parameters to be used to format translatable identifier
     */
    public Object[] getParas() {
        return paras;
    }

}

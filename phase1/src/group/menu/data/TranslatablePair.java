package group.menu.data;

public class TranslatablePair {

    private final String translatable;
    private final Object[] paras;

    public TranslatablePair(String translatable, Object[] paras){
        this.translatable = translatable;
        this.paras = paras;
    }

    public String getTranslatable() {
        return translatable;
    }

    public Object[] getParas() {
        return paras;
    }

}

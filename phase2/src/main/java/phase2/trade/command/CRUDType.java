package phase2.trade.command;

public enum CRUDType {
    CREATE(true), READ(false), UPDATE(true), DELETE(true);

    public boolean hasEffect;

    CRUDType(boolean hasEffect) {
        this.hasEffect = hasEffect;
    }
}

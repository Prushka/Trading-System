package phase2.trade.command;

/**
 * The enum Crud type.
 *
 * @author Dan Lyu
 */
public enum CRUDType {

    /**
     * Create.
     */
    CREATE(true),
    /**
     * Read.
     */
    READ(false),
    /**
     * Update.
     */
    UPDATE(true),
    /**
     * Delete.
     */
    DELETE(true);

    /**
     * If this type will affect any undo operations
     */
    public boolean willAffect;

    CRUDType(boolean willAffect) {
        this.willAffect = willAffect;
    }
}

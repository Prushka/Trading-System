package phase2.trade.command;

/**
 * The base Command class for Commands that will need to handle<p>
 * a toUpdate entity which is the same type as the callback type.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
public abstract class UpdateCommand<T> extends Command<T> {

    /**
     * The entity this Command deals with.
     */
    protected transient T toUpdate;


    /**
     * Sets the entity.
     *
     * @param toUpdate the entity to update
     */
    public void setToUpdate(T toUpdate) {
        this.toUpdate = toUpdate;
    }

}

package phase2.trade.command;

/**
 * The functional interface Command supplier.
 *
 * @param <T> the type parameter
 * @author Dan Lyu
 */
@FunctionalInterface
public interface CommandSupplier<T extends Command<?>> {

    /**
     * Get the Command.
     *
     * @return the Command object
     */
    T get();
    // a general supplier will do the job, but maybe in the future we need more params to be passed into the controller
}

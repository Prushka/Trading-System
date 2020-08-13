package phase2.trade.command;

@FunctionalInterface
public interface CommandSupplier<T extends Command<?>> {

    T get();
    // a general supplier will do the job, but maybe in the future we need more params to be passed into the controller
}

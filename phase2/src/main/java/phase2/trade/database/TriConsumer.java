package phase2.trade.database;

@FunctionalInterface
public interface TriConsumer<A, B, C> {
    void consume(A builder, B criteria, C root);
}

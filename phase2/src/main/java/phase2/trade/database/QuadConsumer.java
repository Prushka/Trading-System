package phase2.trade.database;

@FunctionalInterface
public interface QuadConsumer<A, B, C, D> {
    void consume(A builder, B criteria, C root, D query);
}

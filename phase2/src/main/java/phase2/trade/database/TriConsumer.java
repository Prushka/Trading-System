package phase2.trade.database;

/**
 * The interface Tri consumer.
 *
 * @param <A> the first type
 * @param <B> the second type
 * @param <C> the third type
 * @author Dan Lyu
 */
@FunctionalInterface
public interface TriConsumer<A, B, C> {
    /**
     * Consume 3 objects together.
     *
     * @param a a
     * @param b b
     * @param c c
     */
    void consume(A a, B b, C c);
}

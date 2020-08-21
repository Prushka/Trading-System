package phase2.trade.gateway;

import phase2.trade.Shutdownable;

/**
 * The Gateway pub-sub pattern interface.
 *
 * @author Dan Lyu
 * @see phase2.trade.database.nosql.Redis
 */
public interface GatewayPubSub extends Shutdownable {

    /**
     * Publish a message.
     *
     * @param message the message
     */
    void publish(String message);

    /**
     * Subscribe to the channel.
     */
    void subscribe();
}

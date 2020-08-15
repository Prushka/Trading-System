package phase2.trade.gateway;

import phase2.trade.Shutdownable;

public interface PubSub extends Shutdownable {

    void publish(String message);

    void subscribe();
}

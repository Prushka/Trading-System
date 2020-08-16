package phase2.trade.refresh;

import phase2.trade.ShutdownHook;
import phase2.trade.config.ConfigBundle;
import phase2.trade.database.nosql.Redis;
import phase2.trade.gateway.GatewayPubSub;

import java.util.*;

public class ReReReRe { // observable pub sub?

    private final GatewayPubSub gatewayPubSub;

    Map<String, Refreshable> refreshables = new HashMap<>();

    Map<String, Reloadable> reloadables = new HashMap<>();

    public ReReReRe(ConfigBundle configBundle, ShutdownHook shutdownHook) {
        gatewayPubSub = new Redis(configBundle.getRedisConfig(), reloadables);
        shutdownHook.addShutdownables(gatewayPubSub);
    }

    public void subscribeRefreshable(String key, Refreshable t) {
        this.refreshables.put(key, t);
    }

    public void subscribeReloadable(String key, Reloadable t) {
        this.reloadables.put(key, t);
    }

    public void publishGateway(Class<?>... effectedControllers) {
        publishGateway(getStringRepresentation(effectedControllers));
    }

    public void publishGateway(String message) {
        gatewayPubSub.publish(message);
    }

    public void publish(ReType reType, Class<?>... effectedControllers) {
        publish(reType, getStringRepresentation(effectedControllers));
    }

    public void publish(ReType reType, String message) {
        switch (reType) {
            case RELOAD:
                Collection<Reloadable> effectedReloads = getEffected(reloadables, message);
                effectedReloads.forEach(Reloadable::reload);
                break;
            case REFRESH:
                Collection<Refreshable> effectedRefreshes = getEffected(refreshables, message);
                effectedRefreshes.forEach(Refreshable::refresh);
                break;
        }
    }

    private <R> Collection<R> getEffected(Map<String, R> map, String message) {
        Set<R> extract = new HashSet<>();
        for (String clazzSimple : message.split(",", -1)) {
            if (map.containsKey(clazzSimple)) {
                extract.add(map.get(clazzSimple));
            }
        }
        return extract;
    }


    private String getStringRepresentation(Class<?>[] effectedControllers) {
        StringBuilder simpleRepresentation = new StringBuilder();
        for (Class<?> clazz : effectedControllers) {
            simpleRepresentation.append(clazz.getSimpleName()).append(",");
        }
        return simpleRepresentation.toString();
    }

}

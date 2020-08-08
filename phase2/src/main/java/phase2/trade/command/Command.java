package phase2.trade.command;

import phase2.trade.gateway.Callback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.user.Permission;
import phase2.trade.user.PermissionSet;
import phase2.trade.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Command<T> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    User operator;

    Class<T> clazz;

    @ElementCollection
    Set<Long> effectedIds = new HashSet<>();

    transient GatewayBundle gatewayBundle;

    public Command(GatewayBundle gatewayBundle, Class<T> clazz, User operator) {
        this.clazz = clazz;
        this.operator = operator;
        this.gatewayBundle = gatewayBundle;
    }

    public Command() {

    }

    public boolean checkPermission() {
        boolean all = true;
        for (Permission permissionRequired : getPermissionRequired().getPerm()) {
            all = all && operator.hasPermission(permissionRequired);
        }
        return all;
    }

    public abstract PermissionSet getPermissionRequired();

    public abstract void execute(Callback<T> callback, String... args);

    public abstract void undo();

    public abstract void redo();

    private void save() {
        gatewayBundle.getCommandGateway().submitSessionWithTransaction(() -> gatewayBundle.getCommandGateway().add(getThis()));
    }

    public Command<T> getThis() {
        return this;
    }

    public GatewayBundle getGatewayBundle() {
        return gatewayBundle;
    }

    public void setGatewayBundle(GatewayBundle gatewayBundle) {
        this.gatewayBundle = gatewayBundle;
    }

    public void addEffectedId(Long id) {
        effectedIds.add(id);
    }

}
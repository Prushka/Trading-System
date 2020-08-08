package phase2.trade.item.command;

import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.CRUDType;
import phase2.trade.gateway.EntityBundle;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.permission.Permission;
import phase2.trade.permission.PermissionSet;
import phase2.trade.user.RegularUser;

import javax.persistence.Entity;

@Entity
public class AlterWillingness extends ItemCommand<Item> {

    private Long itemId;

    private Willingness newWillingness;

    private Willingness oldWillingness;

    private transient RegularUser operator;

    public AlterWillingness(EntityBundle entityBundle, RegularUser operator, Willingness newWillingness, Long itemId) {
        super(entityBundle, operator);
        this.itemId = itemId;
        this.operator = operator;
        this.newWillingness = newWillingness;
        addEffectedId(itemId);
    }

    public AlterWillingness() {
        super();
    }

    @Override
    public void execute(StatusCallback<Item> callback, String... args) {
        if (!checkPermission(callback)) {
            return;
        }
        entityBundle.getItemGateway().submitTransaction(() -> {
            Item item = findItemByIdSyncInsideItemGateway(itemId);
            oldWillingness = item.getWillingness();
            item.setWillingness(newWillingness);
            entityBundle.getItemGateway().update(item);
            callback.call(item, ResultStatus.SUCCEEDED);
        });
    }

    @Override
    public PermissionSet getPermissionRequired() {
        return new PermissionSet(Permission.ADD_ITEM);
    }


    @Override
    public void undo() {
    }

    @Override
    public void redo() {

    }

    @Override
    public CRUDType getCRUDType() {
        return CRUDType.UPDATE;
    }
}

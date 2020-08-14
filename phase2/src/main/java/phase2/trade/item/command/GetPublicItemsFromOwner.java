package phase2.trade.item.command;

import phase2.trade.Configurer;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.callback.status.StatusSucceeded;
import phase2.trade.command.CRUDType;
import phase2.trade.command.CommandProperty;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.permission.Permission;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.Entity;
import java.util.List;

@Entity
@CommandProperty(crudType = CRUDType.READ, undoable = true,
        persistent = true, permissionSet = {Permission.BROWSE_MARKET})
public class GetPublicItemsFromOwner extends ItemCommand<List<Item>> {

    private transient Long itemOwnerUID;

    @Override
    public void execute(ResultStatusCallback<List<Item>> callback, String... args) {
        if (!checkPermission(callback)) return;
        getEntityBundle().getItemGateway().submitSession(gateway -> {
            List<Item> items = gateway.findPublicItemsFromOwner(itemOwnerUID);
            callback.call(items, new StatusSucceeded());
        });
    }

    public static void main(String[] args) {
        Configurer configurer = new Configurer();
        configurer.mockDashboardRegister("username","12345678");
        GetPublicItemsFromOwner command = configurer.getCommandFactory().getCommand(GetPublicItemsFromOwner::new, c -> c.setItemOwnerUID(2L));
        command.execute(new ResultStatusCallback<List<Item>>() {
            @Override
            public void call(List<Item> result, ResultStatus status) {
            }
        });
    }

    @Override
    protected void undoUnchecked() {
    }

    public void setItemOwnerUID(Long itemOwnerUID) {
        this.itemOwnerUID = itemOwnerUID;
    }
}

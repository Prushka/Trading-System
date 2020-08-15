package phase2.trade.item.controller;

import javafx.fxml.Initializable;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.GeneralTableViewController;
import phase2.trade.database.TriConsumer;
import phase2.trade.item.Item;
import phase2.trade.item.ItemEditor;
import phase2.trade.item.command.UpdateInventoryItems;

import java.util.ArrayList;
import java.util.List;

public class ItemController extends GeneralTableViewController<Item> implements Initializable {

    public ItemController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable);
    }

    protected void shortenAlter(Item item, String newValue, StatusCallback statusCallback, TriConsumer<ItemEditor, String, StatusCallback> consumer) {
        consumer.consume(new ItemEditor(item), newValue, statusCallback);
        updateItem(item);
    }

    protected void updateItem(Item item) {
        List<Item> items = new ArrayList<>();
        items.add(item);
        updateItem(items);
    }

    protected void updateItem(List<Item> items) {
        disableButtons(true);
        Command<?> command = getCommandFactory().getCommand(UpdateInventoryItems::new, c -> {
            c.setItemsToUpdate(items);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setAfter(() -> {
                disableButtons(false);
                tableView.refresh();
            });
            resultStatus.handle(getPopupFactory());
        });
    }
}

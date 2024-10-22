package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.command.GetAllItems;
import phase2.trade.item.command.RemoveItem;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * The Item manage controller used by administrative users.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_table_view.fxml")
public class ItemManageController extends ItemController implements Initializable {

    /**
     * Constructs a new Item manage controller.
     *
     * @param controllerResources the controller resources
     */
    public ItemManageController(ControllerResources controllerResources) {
        super(controllerResources, true, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<Item>> getItems = getCommandFactory().getCommand(GetAllItems::new);
        getItems.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(FXCollections.observableArrayList(result));
                afterFetch();
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    @Override
    public void reload() {
        Command<List<Item>> getItems = getCommandFactory().getCommand(GetAllItems::new);
        getItems.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                reloadNewDisplayData(result);
            });
            resultStatus.handle(getNotificationFactory());
        });
        super.reload();
    }

    private void afterFetch() {
        addNameColumn(true);
        addDescriptionColumn(true);
        addOwnershipColumn(true);
        addQuantityColumn(true);
        addPriceColumn(true);
        addCategoryColumn(true);
        addWillingnessColumn(true);
        addOwnerColumn();
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes(true);

        JFXButton deleteButton = new JFXButton("Delete");

        deleteButton.setOnAction(e -> displayData.removeAll(getSelected()));

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> command.setItemIds(idsRemoved)), Item::getUid);

        Button reviewItems = new JFXButton("Mark selected items as reviewItems");
        reviewItems.setOnAction(event -> shortenAlterOfSelected(getSelected(), Ownership.OWNER.name(), s -> {
        }, ItemEditor::alterOwnership));
        addButton(reviewItems, deleteButton);
        tableViewGenerator.build();
    }

}

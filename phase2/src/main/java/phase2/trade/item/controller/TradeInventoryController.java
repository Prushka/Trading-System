package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.window.GeneralSplitAlert;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class TradeInventoryController extends ItemController implements Initializable {

    private final ItemListType itemListType = ItemListType.INVENTORY;

    private final Predicate<Item> predicate;

    public TradeInventoryController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable, Predicate<Item> predicate) {
        super(controllerResources, ifMultipleSelection, ifEditable);
        this.predicate = predicate;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        Collection<Item> items = getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems().stream().filter(predicate).collect(Collectors.toList());
        setDisplayData(items);

        addNameColumn(true);
        addDescriptionColumn(true);
        addQuantityColumn(true);
        addPriceColumn(true);
        addCategoryColumn(true);
        addWillingnessColumn(true);
        tableViewGenerator.build();
    }

}

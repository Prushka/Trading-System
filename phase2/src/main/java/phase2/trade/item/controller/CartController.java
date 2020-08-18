package phase2.trade.item.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.itemlist.ItemListType;
import phase2.trade.trade.controller.TradeDragDropController;

import java.net.URL;
import java.util.ResourceBundle;

;

public class CartController extends ItemController implements Initializable {

    private final ItemListType itemListType = ItemListType.CART;

    public CartController(ControllerResources controllerResources) {
        super(controllerResources, true, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));

        addNameColumn(false);
        addDescriptionColumn(false);
        addOwnershipColumn(false);
        addQuantityColumn(false);
        addPriceColumn(false);
        addCategoryColumn(false);
        addWillingnessColumn(false);
        addOwnerColumn();
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes(false);

        Button checkOut = getNodeFactory().getDefaultFlatButton("Check Out");
        Button remove = getNodeFactory().getDefaultFlatButton("Remove");

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        remove.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            displayData.removeAll(itemsSelected);
        });

        checkOut.setOnAction(e -> {
            TradeDragDropController tradeDragDropController = new TradeDragDropController(getControllerResources(), getSelected());
            getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(tradeDragDropController));
        });

        addButton(checkOut, remove);
        tableViewGenerator.build();
    }
}

package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.command.RemoveItem;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class CartTableController extends ItemController implements Initializable {

    private final ItemListType itemListType;

    public CartTableController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, false);
        this.itemListType = itemListType;
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
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes();

        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton trade = new JFXButton("Trade");

        buttonPane.getChildren().addAll(deleteButton, trade);

        buttonsToDisable = FXCollections.observableArrayList(deleteButton, trade);

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            displayData.removeAll(itemsSelected);
        });

        tableViewGenerator.build();
    }
}

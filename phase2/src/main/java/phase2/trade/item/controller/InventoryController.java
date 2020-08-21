package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import phase2.trade.alert.SplitAlert;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToInventory;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.itemlist.ItemListType;
import phase2.trade.user.command.ReloadUser;
import phase2.trade.view.NodeFactory;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class InventoryController extends ItemController implements Initializable {

    private final ItemListType itemListType = ItemListType.INVENTORY;


    public InventoryController(ControllerResources controllerResources) {
        super(controllerResources, true, true);
    }

    @Override
    public void refresh() {
        reloadNewDisplayData(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems());
        super.refresh();
    }

    // Why reload here:
    // If administrative edits an Item in this person's inventory from ANOTHER application
    // The inventory will be reloaded
    @Override
    public void reload() {
        ReloadUser reloadUser = getCommandFactory().getCommand(ReloadUser::new);
        reloadUser.execute((result, status) -> {
            status.setSucceeded(this::refresh);
            status.handle(getNotificationFactory());
        });
        super.reload();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems());

        addNameColumn(true);
        addDescriptionColumn(true);
        addOwnershipColumn(false);
        addQuantityColumn(true);
        addPriceColumn(true);
        addCategoryColumn(true);
        addWillingnessColumn(true);
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes(true);

        Button addButton = new JFXButton("Add");
        Button deleteButton = new JFXButton("Delete");
        Button privateButton = new JFXButton("I wanna this item to be private");
        Button sellButton = new JFXButton("I wanna sell them");
        Button lendButton = new JFXButton("I wanna lend them");


        deleteButton.setOnAction(e -> displayData.removeAll(getSelected()));

        addButton(addButton, deleteButton, sellButton, lendButton, privateButton);

        sellButton.setOnAction(event -> shortenAlter(Willingness.SELL.name(), s -> {
        }, ItemEditor::alterWillingness));
        privateButton.setOnAction(event -> shortenAlter(Willingness.Private.name(), s -> {
        }, ItemEditor::alterWillingness));
        lendButton.setOnAction(event -> shortenAlter(Willingness.LEND.name(), s -> {
        }, ItemEditor::alterWillingness));

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        addButton.setOnAction(event -> {

            SplitAlert addItemAlert = getNotificationFactory().splitAlert("Add Item", "");
            TextField enterItemName = getNodeFactory().getDefaultTextField("Item Name");
            TextField enterItemDescription = getNodeFactory().getDefaultTextField("Item Description");
            TextField enterQuantity = getNodeFactory().getDefaultTextField("Quantity");
            TextField price = getNodeFactory().getDefaultTextField("Price");
            price.setDisable(true);

            ComboBox<String> comboBox = getNodeFactory().getComboBoxByType(NodeFactory.ComboBoxType.Category, null);


            ToggleGroup group = new ToggleGroup();

            RadioButton sellRadio = getNodeFactory().getDefaultRadioButton(Willingness.SELL.language, group);
            RadioButton lendRadio = getNodeFactory().getDefaultRadioButton(Willingness.LEND.language, group);
            RadioButton privateRadio = getNodeFactory().getDefaultRadioButton(Willingness.Private.language, group);
            EventHandler<ActionEvent> willingnessRadioHandler = event1 -> price.setDisable(!sellRadio.isSelected());
            sellRadio.setOnAction(willingnessRadioHandler);
            lendRadio.setOnAction(willingnessRadioHandler);
            privateRadio.setOnAction(willingnessRadioHandler);

            privateRadio.setSelected(true);
            addItemAlert.addLeft(enterItemName, enterItemDescription, enterQuantity, comboBox);
            addItemAlert.addRight(lendRadio, sellRadio, privateRadio, price);
            addItemAlert.setConfirmHandler(event12 -> {
                AddItemToInventory itemCommand = getCommandFactory().getCommand(AddItemToInventory::new,
                        command -> command.setItemListType(itemListType));

                itemCommand.execute((result, resultStatus) -> {
                            resultStatus.setSucceeded(() -> displayData.add(result));
                            resultStatus.handle(getNotificationFactory());
                        }, enterItemName.getText(), enterItemDescription.getText(), comboBox.getSelectionModel().getSelectedItem(), enterQuantity.getText(),
                        Willingness.getByLanguage(((RadioButton) group.getSelectedToggle()).getText()).name(), // this casting cannot be avoided. another approach would be to iterate over all radio buttons
                        price.getText());
            });
            addItemAlert.display();
        });

        tableViewGenerator.build();
    }

}

package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import phase2.trade.alert.GeneralSplitAlert;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.itemlist.ItemListType;
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
    public void reload() {
        // use RefreshUser here
        super.reload();
    }

    // TODO: if view is updated first, then even if the execution fails, the item would disappear. It would reappear if user refreshes this tableview
    //  Also it's not possible to bind the entity to the view since the entity is in database and I don't think it's a good idea to replace all fields in entities to be Properties and Observable
    //  1. We can update entity first without taking benefit from Observable List. So that the ResultStatus can be checked in first place
    //  2. Maybe we can also retrieve necessary elements from the database and store it as a cache. But I don't have time for this. It can take time to implement the caching system
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

            GeneralSplitAlert addItemAlert = getNotificationFactory().splitAlert("Add Item", "");
            TextField enterItemName = getNodeFactory().getDefaultTextField("Item Name");
            TextField enterItemDescription = getNodeFactory().getDefaultTextField("Item Description");
            TextField enterQuantity = getNodeFactory().getDefaultTextField("Quantity");
            TextField price = getNodeFactory().getDefaultTextField("Price");
            price.setDisable(true);

            ComboBox<String> comboBox = getNodeFactory().getComboBoxByType(NodeFactory.ComboBoxType.Category);


            ToggleGroup group = new ToggleGroup();
            putLanguageValue(Willingness.SELL.name(), "sell.willingness");
            putLanguageValue(Willingness.LEND.name(), "lend.willingness");
            putLanguageValue(Willingness.Private.name(), "private.willingness");

            RadioButton sellRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(Willingness.SELL.name()), group);
            RadioButton lendRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(Willingness.LEND.name()), group);
            RadioButton privateRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(Willingness.Private.name()), group);
            EventHandler<ActionEvent> willingnessRadioHandler = event1 -> price.setDisable(!sellRadio.isSelected());
            sellRadio.setOnAction(willingnessRadioHandler);
            lendRadio.setOnAction(willingnessRadioHandler);
            privateRadio.setOnAction(willingnessRadioHandler);

            privateRadio.setSelected(true);
            addItemAlert.addLeft(enterItemName, enterItemDescription, enterQuantity, comboBox);
            addItemAlert.addRight(lendRadio, sellRadio, privateRadio, price);
            addItemAlert.setEventHandler(event12 -> {
                AddItemToItemList itemCommand = getCommandFactory().getCommand(AddItemToItemList::new,
                        command -> command.setItemListType(itemListType));

                itemCommand.execute((result, resultStatus) -> {
                            resultStatus.setSucceeded(() -> displayData.add(result));
                            resultStatus.handle(getNotificationFactory());
                        }, enterItemName.getText(), enterItemDescription.getText(), comboBox.getSelectionModel().getSelectedItem(), enterQuantity.getText(), getValueByLanguage(((RadioButton) group.getSelectedToggle()).getText()),
                        price.getText()); // this casting cannot be avoided. another approach would be to loop through all radio buttons
            });
            addItemAlert.display();
        });

        tableViewGenerator.build();
    }

}

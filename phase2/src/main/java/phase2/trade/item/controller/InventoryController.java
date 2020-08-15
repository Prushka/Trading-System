package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.window.GeneralSplitAlert;

import java.net.URL;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class InventoryController extends ItemController implements Initializable {

    private final ItemListType itemListType;

    public InventoryController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, true);
        this.itemListType = itemListType;
    }

    // TODO: if view is updated first, then even if the execution fails, the item would disappear. It would reappear if user refreshes this tableview
    //  Also it's not possible to bind the entity to the view since the entity is in database and I don't think it's a good idea to replace all fields in entities to be Properties and Observable
    //  1. We can update entity first without taking benefit from Observable List. So that the ResultStatus can be checked in first place
    //  2. Maybe we can also retrieve necessary elements from the database and store it as a cache. But I don't have time for this. It can take time to implement the caching system
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));

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
        addWillingnessCheckBoxes();

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton privateButton = new JFXButton("I wanna this item to be private");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        hBox.getChildren().addAll(addButton, deleteButton, sellButton, lendButton, privateButton);
        buttonsToDisable = FXCollections.observableArrayList(addButton, deleteButton, sellButton, lendButton, privateButton);

        sellButton.setOnAction(event -> shortenAlter(Willingness.Sell.name(), s -> {
        }, ItemEditor::alterWillingness));
        privateButton.setOnAction(event -> shortenAlter(Willingness.Private.name(), s -> {
        }, ItemEditor::alterWillingness));
        lendButton.setOnAction(event -> shortenAlter(Willingness.Lend.name(), s -> {
        }, ItemEditor::alterWillingness));

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        addButton.setOnAction(event -> {

            GeneralSplitAlert addItemAlert = getPopupFactory().splitAlert("Add Item", "");
            TextField enterItemName = getNodeFactory().getDefaultTextField("Item Name");
            TextField enterItemDescription = getNodeFactory().getDefaultTextField("Item Description");
            TextField enterQuantity = getNodeFactory().getDefaultTextField("Quantity");
            TextField price = getNodeFactory().getDefaultTextField("Price");
            price.setDisable(true);

            ComboBox<String> comboBox = getNodeFactory().getComboBox(NodeFactory.ComboBoxType.Category);


            ToggleGroup group = new ToggleGroup();
            putLanguageValue(Willingness.Sell.name(), "sell.willingness");
            putLanguageValue(Willingness.Lend.name(), "lend.willingness");
            putLanguageValue(Willingness.Private.name(), "private.willingness");

            RadioButton sellRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(Willingness.Sell.name()), group);
            RadioButton lendRadio = getNodeFactory().getDefaultRadioButton(getLanguageByValue(Willingness.Lend.name()), group);
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
                    resultStatus.handle(getPopupFactory());
                }, enterItemName.getText(), enterItemDescription.getText(), comboBox.getSelectionModel().getSelectedItem(), enterQuantity.getText(), getValueByLanguage(((RadioButton) group.getSelectedToggle()).getText())); // this casting cannot be avoided. another approach would be to loop through all radio buttons
            });
            addItemAlert.display();
        });

        tableViewGenerator.build();
    }

}

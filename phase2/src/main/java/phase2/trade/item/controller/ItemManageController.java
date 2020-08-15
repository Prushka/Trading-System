package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.GeneralTableViewController;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemEditor;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.item.command.UpdateInventoryItems;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.window.GeneralSplitAlert;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class ItemManageController extends GeneralTableViewController<Item> implements Initializable {

    private final ItemListType itemListType;

    public ItemManageController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, false);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));

        tableViewGenerator.addColumn("Name", "name")
                .addColumn("Description", "description",
                        getConfigBundle().getUiConfig().getItemDescriptionPrefWidth()).addColumn("Category", "category")
                .addColumn("Ownership", "ownership")
                .addColumn("Quantity", "quantity")
                .addColumn("Price", "price")
                .addColumn("Willingness", "willingness")
                .addColumn("UID", "uid");

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton privateButton = new JFXButton("I wanna this item to be private");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        hBox.getChildren().addAll(addButton, deleteButton, sellButton, lendButton, privateButton);
        buttonsToDisable = FXCollections.observableArrayList(addButton, deleteButton, sellButton, lendButton, privateButton);

        sellButton.setOnAction(getWillingnessHandler(Willingness.Sell));
        privateButton.setOnAction(getWillingnessHandler(Willingness.Private));
        lendButton.setOnAction(getWillingnessHandler(Willingness.Lend));


        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        addSearchField("Search Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        addSearchField("Search Description", (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
        });

        addComboBox(
                FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))),
                "Category", "ALL",
                (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));


        JFXCheckBox lend = new JFXCheckBox("Wish To Lend");
        JFXCheckBox sell = new JFXCheckBox("Wish To Sell");
        JFXCheckBox privateCheckBox = new JFXCheckBox("Private");

        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.Lend))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.Sell))
                .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.Private));

        getPane("topBar").getChildren().addAll(lend, sell, privateCheckBox);
        lend.setSelected(true);
        sell.setSelected(true);
        privateCheckBox.setSelected(true);

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


    public EventHandler<ActionEvent> getWillingnessHandler(Willingness willingness) {
        return event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) {
                getPopupFactory().toast(3, "You didn't select anything", "CLOSE");
                return;
            }

            ItemEditor itemEditor = new ItemEditor(itemsSelected);
            itemEditor.alterWillingness(willingness, resultStatus -> {
                resultStatus.setSucceeded(() -> updateItem(itemsSelected));
                resultStatus.handle(getPopupFactory());
            });

        };
    }

    private void updateItem(List<Item> items) {
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
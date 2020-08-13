package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import phase2.trade.command.Command;
import phase2.trade.controller.AddItemController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemEditor;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.item.command.UpdateInventoryItems;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class ItemTableController extends GeneralTableViewController<Item> implements Initializable {

    private final ItemListType itemListType;

    public ItemTableController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, false);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));

        tableViewGenerator.addColumn("Name", "name").addColumn("Description", "description").addColumn("Category", "category")
                .addColumn("Ownership", "ownership").addColumn("Quantity", "quantity").addColumn("Price", "price").addColumn("Willingness", "willingness").addColumn("UID", "uid");

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton privateButton = new JFXButton("I wanna this item to be private");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        buttons.getChildren().addAll(addButton, deleteButton, sellButton, lendButton, privateButton);
        buttonsToDisable = FXCollections.observableArrayList(addButton, deleteButton, sellButton, lendButton, privateButton);

        sellButton.setOnAction(getWillingnessHandler(Willingness.SELL));
        privateButton.setOnAction(getWillingnessHandler(Willingness.NOPE));
        lendButton.setOnAction(getWillingnessHandler(Willingness.LEND));


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

        JFXTextField searchDescription = new JFXTextField();
        searchDescription.setPromptText("Search Description");
        searchDescription.setLabelFloat(true);

        JFXComboBox<String> category = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))));
        category.setPromptText("Category");
        category.getItems().add("ALL");
        category.setLabelFloat(true);


        JFXCheckBox lend = new JFXCheckBox("Wish To Lend");
        JFXCheckBox sell = new JFXCheckBox("Wish To Sell");
        JFXCheckBox privateCheckBox = new JFXCheckBox("Private");

        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.LEND))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.SELL))
                .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.NOPE))
                .addComboBox(category, (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));

        getPane("topBar").getChildren().setAll(category, lend, sell, privateCheckBox);
        lend.setSelected(true);
        sell.setSelected(true);
        privateCheckBox.setSelected(true);

        addButton.setOnAction(event -> {
            addWindow(displayData);
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

    public void addWindow(ObservableList<Item> displayData) {
        AddItemController addItemController = new AddItemController(getControllerResources(), itemListType, displayData);
        getSceneManager().loadPane(addItemController);
    }

}

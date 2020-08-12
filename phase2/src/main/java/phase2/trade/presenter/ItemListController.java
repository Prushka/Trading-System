package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.ItemEditor;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.UpdateInventoryItems;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "item_list.fxml")
public class ItemListController extends AbstractController implements Initializable {

    private final ItemListType itemListType;

    public TableView<Item> tableView;

    public HBox buttons;

    private List<Button> buttonsToDisable;

    private ObservableList<Item> displayData;

    public ItemListController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayData = FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getListOfItems());


        TableViewGenerator<Item> tableViewGenerator = new TableViewGenerator<>(displayData, 100, tableView);
        tableViewGenerator.addColumn("Name", "name").addColumn("Description", "description").addColumn("Category", "category")
                .addColumn("Ownership", "ownership").addColumn("Quantity", "quantity").addColumn("Price", "price").addColumn("Willingness", "willingness").addColumn("UID", "uid");

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        buttons.getChildren().addAll(addButton, deleteButton, sellButton, lendButton);

        sellButton.setOnAction(getWillingnessHandler(Willingness.SELL));

        lendButton.setOnAction(getWillingnessHandler(Willingness.LEND));

        buttonsToDisable = FXCollections.observableArrayList(addButton, deleteButton, sellButton, lendButton);

        displayData.addListener((ListChangeListener<Item>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    disableButtons(true);
                    Command<?> remove = getCommandFactory().getCommand(RemoveItem::new, command -> {
                        command.setItemListType(itemListType);
                        command.setItemIds(getItemIdsFrom(
                                c.getRemoved()));
                    });
                    remove.execute((result, resultStatus) -> {
                        Platform.runLater(() -> {
                            resultStatus.setAfter(() -> disableButtons(false));
                            resultStatus.handle(getPopupFactory());
                        });
                    });
                }
            }
        });

        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            displayData.removeAll(itemsSelected);
        });

        JFXTextField searchName = new JFXTextField();
        searchName.setPromptText("Search Name");
        searchName.setLabelFloat(true);

        JFXTextField searchDescription = new JFXTextField();
        searchDescription.setPromptText("Search Description");
        searchDescription.setLabelFloat(true);

        getPane("topBar").getChildren().setAll(searchName, searchDescription);

        tableViewGenerator.addSearch(searchName, (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        tableViewGenerator.addSearch(searchDescription, (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
        });

        addButton.setOnAction(event -> {
            addWindow(displayData);
        });

        tableViewGenerator.build();
    }


    public EventHandler<ActionEvent> getWillingnessHandler(Willingness willingness) {
        return event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;

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

    private Set<Long> getItemIdsFrom(List<? extends Item> list) {
        Set<Long> ids = new HashSet<>();
        for (Item item : list) {
            ids.add(item.getUid());
        }
        return ids;
    }

    private ObservableList<Item> getSelected() {
        ObservableList<Item> itemsSelected;
        itemsSelected = tableView.getSelectionModel().getSelectedItems();
        return itemsSelected;
    }

    public void addWindow(ObservableList<Item> displayData) {
        AddItemController addItemController = new AddItemController(getControllerResources(), itemListType, displayData);
        getSceneManager().loadPane(addItemController);
    }

    private void disableButtons(boolean value) {
        for (Button button : buttonsToDisable) {
            button.setDisable(value);
        }
    }
}

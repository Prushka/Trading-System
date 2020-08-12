package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AlterWillingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class ItemListController extends AbstractController implements Initializable {

    private ItemListType itemListType;

    public TableView<Item> tableView;

    public HBox buttons;

    public JFXTextField searchName;

    public ItemListController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Item> displayData = FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getListOfItems());

        TableViewGenerator<Item> tableViewGenerator = new TableViewGenerator<>(displayData, 100, tableView);
        tableViewGenerator.addColumn("Name", "name").addColumn("Description", "description").addColumn("Category", "category")
                .addColumn("Ownership", "ownership").addColumn("Quantity", "quantity").addColumn("Price", "price").addColumn("Willingness", "willingness");


        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        buttons.getChildren().addAll(addButton, deleteButton, sellButton, lendButton);

        sellButton.setOnAction(getWillingnessHandler(sellButton, Willingness.WISH_TO_SELL));

        lendButton.setOnAction(getWillingnessHandler(sellButton, Willingness.WISH_TO_LEND));

        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            deleteButton.setDisable(true);

            Command<Long[]> remove = getCommandFactory().getCommand(RemoveItem::new, c -> {
                c.setItemListType(itemListType);
                c.setItemIds(getItemIdsFrom(itemsSelected));
            });
            remove.execute((result, resultStatus) -> {
                Platform.runLater(() -> {
                    resultStatus.setAfter(() -> deleteButton.setDisable(false));
                    resultStatus.setSucceeded(() -> itemsSelected.forEach(displayData::remove));
                    resultStatus.handle(getPopupFactory());
                });
            });
        });

        tableViewGenerator.addSearch(searchName, (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        addButton.setOnAction(event -> {
            addWindow(displayData);
        });

        tableViewGenerator.build();
    }


    public EventHandler<ActionEvent> getWillingnessHandler(JFXButton button, Willingness willingness) {
        return event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            button.setDisable(true);
            Command<Item> command = getCommandFactory().getCommand(AlterWillingness::new, c -> {
                c.setItemIds(getItemIdsFrom(itemsSelected));
                c.setNewWillingness(willingness);
            });
            command.execute((result, resultStatus) -> {
                resultStatus.setAfter(() -> button.setDisable(false));
                resultStatus.setSucceeded(() -> itemsSelected.forEach(item -> item.setWillingness(willingness)));
                resultStatus.handle(getPopupFactory());
            });
        };
    }

    private Set<Long> getItemIdsFrom(ObservableList<Item> observableList) {
        Set<Long> ids = new HashSet<>();
        for (Item item : observableList) {
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
        getSceneManager().loadPane("add_item.fxml", addItemController);
    }
}

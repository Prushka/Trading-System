package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AlterWillingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.RegularUser;
import phase2.trade.view.TableViewGenerator;
import phase2.trade.view.TextFieldPredicate;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;

public class ItemListController extends AbstractController implements Initializable {

    private final ItemList itemList;

    public TableView<Item> tableView;

    public HBox buttons;

    public JFXTextField searchName;

    public ItemListController(GatewayBundle gatewayBundle, ItemList itemList) {
        super(gatewayBundle);
        this.itemList = itemList;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeAbstractController(tableView);
        ObservableList<Item> displayData = FXCollections.observableArrayList(itemList.getListOfItems());

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

            Command<Long[]> remove = new RemoveItem(gatewayBundle, (RegularUser) itemList.getOwner(), itemList.getInventoryType(), getItemIdsFrom(itemsSelected)); // TODO: avoid casting
            remove.execute((result, resultStatus) -> {
                Platform.runLater(() -> {
                    itemsSelected.forEach(displayData::remove);
                    deleteButton.setDisable(false);
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
            Command<Item> command = new AlterWillingness(gatewayBundle, (RegularUser) itemList.getOwner(), willingness, getItemIdsFrom(itemsSelected));
            command.execute((result, resultStatus) -> Platform.runLater(() -> {
                itemsSelected.forEach(item -> item.setWillingness(willingness));
                button.setDisable(false);
            }));
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
        AddItemController addItemController = new AddItemController(gatewayBundle, (RegularUser) itemList.getOwner(), itemList.getInventoryType(), displayData);
        getSceneFactory().loadPane("add_item.fxml", addItemController);
    }
}

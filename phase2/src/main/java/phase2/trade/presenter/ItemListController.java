package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AlterWillingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.RegularUser;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;

public class ItemListController extends AbstractController implements Initializable {

    private final ItemListType itemListType;
    private RegularUser user;

    public TableView<Item> tableView;

    public VBox root;

    private GatewayBundle gatewayBundle;

    public ItemListController(GatewayBundle gatewayBundle, RegularUser user, ItemListType itemListType) {
        super(gatewayBundle);
        this.user = user;
        this.gatewayBundle = gatewayBundle;
        this.itemListType = itemListType;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Item> displayData = FXCollections.observableArrayList(user.getItemList(itemListType).getListOfItems());

        TableViewGenerator<Item> tableViewGenerator = new TableViewGenerator<>(displayData, 150);
        tableViewGenerator.addColumn("Name", "name").addColumn("Description", "description").addColumn("Category", "category")
                .addColumn("Ownership", "ownership").addColumn("Quantity", "quantity").addColumn("Price", "price").addColumn("Willingness", "willingness");

        tableView = tableViewGenerator.build();

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton sellButton = new JFXButton("I wanna sell them");
        JFXButton lendButton = new JFXButton("I wanna lend them");

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10)); // padding around entire layout
        hbox.setSpacing(10);
        hbox.getChildren().addAll(addButton, deleteButton, sellButton, lendButton);

        sellButton.setOnAction(getWillingnessHandler(sellButton, Willingness.WISH_TO_SELL));

        lendButton.setOnAction(getWillingnessHandler(sellButton, Willingness.WISH_TO_LEND));

        addButton.setOnAction(event -> {
            addWindow(displayData);
        });
        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            deleteButton.setDisable(true);

            Command<Long[]> remove = new RemoveItem(gatewayBundle, user, itemListType, getItemIdsFrom(itemsSelected));
            remove.execute((result, resultStatus) -> {
                Platform.runLater(() -> {
                    itemsSelected.forEach(displayData::remove);
                    deleteButton.setDisable(false);
                });
            });
        });

        root.getChildren().addAll(tableView, hbox);
    }

    public EventHandler<ActionEvent> getWillingnessHandler(JFXButton button, Willingness willingness) {
        return event -> {
            ObservableList<Item> itemsSelected = getSelected();
            if (itemsSelected.size() == 0) return;
            button.setDisable(true);
            Command<Item> command = new AlterWillingness(gatewayBundle, user, willingness, getItemIdsFrom(itemsSelected));
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
        AddItemController addItemController = new AddItemController(gatewayBundle, user, itemListType, displayData);
        loadPane("add_item.fxml", addItemController);
    }
}

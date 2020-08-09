package phase2.trade.presenter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
import phase2.trade.controller.ItemAddController;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

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
        TableColumn<Item, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Item, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setMinWidth(100);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Item, String> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        ObservableList<Item> displayData = FXCollections.observableArrayList(user.getItemList(itemListType).getListOfItems());

        tableView = new TableView<>();
        tableView.setItems(displayData);
        tableView.getColumns().addAll(FXCollections.observableArrayList(nameColumn, descriptionColumn, quantityColumn));

        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        TextField priceInput = new TextField();
        priceInput.setPromptText("Description");

        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");

        Button addButton = new Button("Add");
        Button deleteButton = new Button("Delete");

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10)); // padding around entire layout
        hbox.setSpacing(10);
        hbox.getChildren().addAll(nameInput, priceInput, quantityInput, addButton, deleteButton);

        addButton.setOnAction(event -> {
            addWindow(displayData);
        });
        deleteButton.setOnAction(event -> {
            deleteButton.setDisable(true);

            ObservableList<Item> itemsSelected;
            itemsSelected = tableView.getSelectionModel().getSelectedItems();
            Set<Long> ids = new HashSet<>();
            for (Item item : itemsSelected) {
                ids.add(item.getUid());
            }
            Command<Long[]> remove = new RemoveItem(gatewayBundle, user, itemListType, ids);
            remove.execute((result, resultStatus) -> {
                Platform.runLater(() -> {
                    itemsSelected.forEach(displayData::remove);
                    deleteButton.setDisable(false);
                });
            });
        });

        root.getChildren().addAll(tableView, hbox);
    }

    public void addWindow(ObservableList<Item> displayData) {
        ItemAddController itemAddController = new ItemAddController(gatewayBundle, user, itemListType, displayData);
        loadPane("add_item.fxml", itemAddController);
    }
}
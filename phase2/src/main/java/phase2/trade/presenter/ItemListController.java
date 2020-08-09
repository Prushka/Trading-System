package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.RegularUser;

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

    private TableColumn<Item, String> getTableColumn(String name, String fieldName) {
        TableColumn<Item, String> column = new TableColumn<>(name);
        column.setMinWidth(100);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<Item, String> nameColumn = getTableColumn("Name", "name");
        TableColumn<Item, String> descriptionColumn = getTableColumn("Description", "description");
        TableColumn<Item, String> categoryColumn = getTableColumn("Category", "category");
        TableColumn<Item, String> ownershipColumn = getTableColumn("Ownership", "ownership");
        TableColumn<Item, String> quantityColumn = getTableColumn("Quantity", "quantity");
        TableColumn<Item, String> priceColumn = getTableColumn("Price", "price");
        TableColumn<Item, String> willingnessColumn = getTableColumn("Willingness", "willingness");

        ObservableList<Item> displayData = FXCollections.observableArrayList(user.getItemList(itemListType).getListOfItems());

        tableView = new TableView<>();
        tableView.setItems(displayData);
        tableView.getColumns().addAll(FXCollections.observableArrayList(nameColumn, descriptionColumn, categoryColumn, ownershipColumn, quantityColumn, willingnessColumn, priceColumn));

        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");
        nameInput.setMinWidth(100);

        TextField priceInput = new TextField();
        priceInput.setPromptText("Description");

        TextField quantityInput = new TextField();
        quantityInput.setPromptText("Quantity");

        JFXButton addButton = new JFXButton("Add");
        JFXButton deleteButton = new JFXButton("Delete");

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10)); // padding around entire layout
        hbox.setSpacing(10);
        hbox.getChildren().addAll(addButton, deleteButton);

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
        AddItemController addItemController = new AddItemController(gatewayBundle, user, itemListType, displayData);
        loadPane("add_item.fxml", addItemController);
    }
}

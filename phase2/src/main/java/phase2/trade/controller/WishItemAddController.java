package phase2.trade.controller;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import phase2.trade.callback.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.item.command.AlterWillingness;
import phase2.trade.item.command.GetItems;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Stream;

public class WishItemAddController extends AbstractController implements Initializable {
    private final ItemListType itemListType;
    private RegularUser user;

    public TableView<Item> tableView;

    public TableView<Item> suggestedItems;

    public VBox root;

    private GatewayBundle gatewayBundle;

    public WishItemAddController(GatewayBundle gatewayBundle, RegularUser user, ItemListType itemListType) {
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
        // Regular User's Items
        TableColumn<Item, String> nameColumn = getTableColumn("Name", "name");
        TableColumn<Item, String> descriptionColumn = getTableColumn("Description", "description");
        TableColumn<Item, String> categoryColumn = getTableColumn("Category", "category");
        TableColumn<Item, String> ownershipColumn = getTableColumn("Ownership", "ownership");
        TableColumn<Item, String> quantityColumn = getTableColumn("Quantity", "quantity");
        TableColumn<Item, String> priceColumn = getTableColumn("Price", "price");
        TableColumn<Item, String> willingnessColumn = getTableColumn("Willingness", "willingness");

        TableColumn<Item, String> suggestedNameColumn = getTableColumn("Name", "name");
        TableColumn<Item, String> suggestedDescriptionColumn = getTableColumn("Description", "description");
        TableColumn<Item, String> suggestedCategoryColumn = getTableColumn("Category", "category");
        TableColumn<Item, String> suggestedPriceColumn = getTableColumn("Price", "price");

        ObservableList<Item> displayData = FXCollections.observableArrayList(user.getItemList(itemListType).getListOfItems());

        tableView = new TableView<>();
        tableView.setItems(displayData);
        tableView.getColumns().addAll(FXCollections.observableArrayList(nameColumn, descriptionColumn, categoryColumn,
                ownershipColumn, quantityColumn, willingnessColumn, priceColumn));

        // Recommending suggested items
        ObservableList<Item> suggestedItemsDisplay = getSuggestions();
        suggestedItems = new TableView<>();
        suggestedItems.setItems(suggestedItemsDisplay);
        tableView.getColumns().addAll(FXCollections.observableArrayList(suggestedNameColumn, suggestedDescriptionColumn,
                suggestedCategoryColumn, suggestedPriceColumn));


        // Adding Items
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

        root.getChildren().addAll(tableView, hbox, suggestedItems);
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

    private Set<Long> getItemIdsFrom(ObservableList<Item> observableList){
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

    private ObservableList<Item> getSuggestions(){
        ObservableList<Item> suggestions = FXCollections.observableArrayList();
        // for (User currUser: gatewayBundle.getEntityBundle().getUserGateway().findAll()){
            // GetItems getItems = new GetItems(gatewayBundle, user, ItemListType.INVENTORY);
            // if (!currUser.equals(this.user)) { // want to suggest items of same category
                // suggestions.addAll(getItems.execute(););
            // }
        // }
        return suggestions;
    }

    public void addWindow(ObservableList<Item> displayData) {
        AddItemController addItemController = new AddItemController(gatewayBundle, user, itemListType, displayData);
        getSceneFactory().loadPane("add_item.fxml", addItemController);
    }
}

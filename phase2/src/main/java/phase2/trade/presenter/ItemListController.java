package phase2.trade.presenter;

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
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddItemToItemList;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ResourceBundle;

public class ItemListController implements Initializable {

    private final ItemListType itemListType;
    private RegularUser user;

    public TableView<Item> tableView;

    public VBox root;

    private GatewayBundle gatewayBundle;

    public ItemListController(GatewayBundle gatewayBundle, RegularUser user, ItemListType itemListType) {
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
        Button saveButton = new Button("Save");

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10)); // padding around entire layout
        hbox.setSpacing(10);
        hbox.getChildren().addAll(nameInput, priceInput, quantityInput, addButton, deleteButton, saveButton);

        addButton.setOnAction(event -> {
            addButton.setDisable(true);
            Command<Item> itemCommand = new AddItemToItemList(gatewayBundle, user, itemListType);
            itemCommand.execute((result, resultStatus) -> {
                if (resultStatus == ResultStatus.NO_PERMISSION) {

                } else {
                    tableView.getItems().add(result);
                }
            }, nameInput.getText(), descriptionColumn.getText(), quantityColumn.getText());
            nameInput.clear();
            priceInput.clear();
            quantityInput.clear();
            addButton.setDisable(false);
        });
        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected;
            itemsSelected = tableView.getSelectionModel().getSelectedItems();
            itemsSelected.forEach(displayData::remove);
        });

        root.getChildren().addAll(tableView, hbox);
    }
}

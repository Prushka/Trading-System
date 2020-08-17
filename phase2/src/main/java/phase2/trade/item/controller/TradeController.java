package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.item.ItemFilter;
import phase2.trade.user.User;
import phase2.trade.view.TableViewGenerator;
import phase2.trade.view.window.AddressAlertController;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "trade.fxml")
public class TradeController extends AbstractController implements Initializable {

    @FXML
    private BorderPane root;

    private final ObservableList<Item> selectedItems;

    @FXML
    private HBox topLeftHBox, topRightHBox;

    @FXML
    private Pane leftTableArea, rightTableArea;

    @FXML
    private VBox left, right;

    @FXML
    private HBox buttonPane;

    Map<User, UserTable> userTables = new HashMap<>();
    AllTable allTable;

    private ComboBox<User> rightComboBox, leftComboBox;

    private final Map<Long, Item> allItems = new HashMap<>();

    public TradeController(ControllerResources controllerResources, ObservableList<Item> selectedItems) {
        super(controllerResources);
        this.selectedItems = FXCollections.observableArrayList(selectedItems);
    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedItems.addAll(new ItemFilter(getAccountManager().getLoggedInUser()).getTradableItems());
        allTable = new AllTable(selectedItems, getControllerResources());
        selectedItems.forEach(item -> allItems.put(item.getUid(), item));

        for (Item item : selectedItems) {
            User owner = item.getOwner();
            if (!userTables.containsKey(owner)) {
                UserTable userTable = new UserTable(owner, getControllerResources());
                userTables.put(owner, userTable);
            }
        }

        setupAllTableDrags(allTable.getTableView());


        userTables.values().forEach(t -> {
            setupUserTableDrags(t.getTableView());
        });

        rightComboBox = getUserComboBox(userTables.keySet());
        rightComboBox.setOnAction(e -> {
            rightTableArea.getChildren().setAll(userTables
                    .get(rightComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });


        BorderPane.setMargin(left, new Insets(0, 10, 0, 0));

        Label willGetFollowingItems = new Label("  will get following items.");

        leftTableArea.getChildren().setAll(allTable.tableViewGenerator.getTableView());
        topRightHBox.getChildren().addAll(rightComboBox, willGetFollowingItems);

        Button tradeButton = new JFXButton("Trade");

        leftComboBox = new JFXComboBox<>();
        leftComboBox.setVisible(false);
        topLeftHBox.getChildren().addAll(leftComboBox);

        buttonPane.getChildren().addAll(tradeButton);


        tradeButton.setOnAction(e -> {
            Map<User, Collection<Item>> userToItemToGet = new HashMap<>();
            for (Map.Entry<User, UserTable> entry : userTables.entrySet()) {
                userToItemToGet.put(entry.getKey(), entry.getValue().getTableView().getItems());
            }
            TradeDetailController tradeDetailController = new TradeDetailController(getControllerResources(), userToItemToGet);
            getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(tradeDetailController));
        });
    }

    private void setupAllTableDrags(TableView<Item> allTableView) {
        setUpDrags(allTableView);
        allTableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasString()) {
                Item item = allItems.get(Long.valueOf(db.getString()));
                if (!allTableView.getItems().contains(item)) {
                    allTableView.getItems().addAll(item);
                    for (UserTable userTable : userTables.values()) {
                        userTable.getTableView().getItems().remove(item);
                    }
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setupUserTableDrags(TableView<Item> tableView) {
        setUpDrags(tableView);
        tableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasString()) {
                Item item = allItems.get(Long.valueOf(db.getString()));
                if (!tableView.getItems().contains(item)) {
                    if (userTables.get(item.getOwner()).getTableView() == tableView) {
                        getPopupFactory().toast(5, "Do not drag items to oneself! This won't form a trade my friend");
                        return;
                    }
                    tableView.getItems().addAll(item);
                    allTable.getTableView().getItems().remove(item);
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }

    private void setUpDrags(TableView<Item> tableView) {

        tableView.setOnDragDetected(event -> {
                    Item selectedItem = tableView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        String itemId = String.valueOf(selectedItem.getUid());
                        Dragboard db = tableView.startDragAndDrop(TransferMode.ANY);
                        ClipboardContent content = new ClipboardContent();
                        content.putString(itemId);
                        db.setContent(content);
                        event.consume();
                    }
                }
        );

        tableView.setOnDragOver(event -> {
                    Dragboard db = event.getDragboard();
                    if (event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
        );
    }

    private ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new UserStringConverter(comboBox));
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(users);
        return comboBox;
    }
}

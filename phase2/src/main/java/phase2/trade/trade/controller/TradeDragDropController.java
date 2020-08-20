package phase2.trade.trade.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.item.ItemFilter;
import phase2.trade.user.User;

import java.net.URL;
import java.util.*;


public class TradeDragDropController extends TradeController {

    private final ObservableList<Item> selectedItems;

    private final Map<Long, Item> allItems = new HashMap<>();

    private final Map<User, UserTable> userTables = new HashMap<>();

    private AllTable allTable;

    public TradeDragDropController(ControllerResources controllerResources, ObservableList<Item> selectedItems) {
        super(controllerResources);
        this.selectedItems = FXCollections.observableArrayList(selectedItems);
    }

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


        userTables.values().forEach(t -> setupUserTableDrags(t.getTableView()));

        rightComboBox = getUserComboBox(userTables.keySet());
        rightComboBox.setOnAction(e -> {
            rightTableArea.getChildren().setAll(userTables
                    .get(rightComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });


        BorderPane.setMargin(left, new Insets(0, 10, 0, 0));

        Label willGetFollowingItems = new Label("  will get following items");

        leftTableArea.getChildren().setAll(allTable.tableViewGenerator.getTableView());
        topRightHBox.getChildren().addAll(rightComboBox, willGetFollowingItems);

        Button tradeButton = new JFXButton("Trade");

        ComboBox<User> leftComboBox = getNodeFactory().getDefaultComboBox(new ArrayList<>());
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
                        getNotificationFactory().toast(5, "Do not drag items to oneself! This won't form a trade my friend");
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
                    if (event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
        );
    }
}

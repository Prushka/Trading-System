package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.ItemFilter;
import phase2.trade.item.Ownership;
import phase2.trade.user.User;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    class UserTable extends AllTable {
        User owner;

        UserTable(User owner) {
            super(FXCollections.observableArrayList());
            this.owner = owner;
        }
    }

    class AllTable {
        TableViewGenerator<Item> tableViewGenerator;
        TargetUserTableController targetUserTableController;

        AllTable(ObservableList<Item> items) {
            tableViewGenerator = new TableViewGenerator<>(items);
            tableViewGenerator.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViewGenerator.getTableView().setPrefWidth(650);
            targetUserTableController = new TargetUserTableController(getControllerResources(), tableViewGenerator);
            tableViewGenerator.getTableView().setItems(items);
        }

        TableView<Item> getTableView() {
            return tableViewGenerator.getTableView();
        }
    }

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
        allTable = new AllTable(selectedItems);
        selectedItems.forEach(item -> allItems.put(item.getUid(), item));

        for (Item item : selectedItems) {
            User owner = item.getOwner();
            if (!userTables.containsKey(owner)) {
                UserTable userTable = new UserTable(owner);
                userTables.put(owner, userTable);
            }
        }

        setUpDrags(allTable.getTableView());

        userTables.values().forEach(t -> {
            setUpDrags(t.getTableView());
        });

        rightComboBox = getUserComboBox(userTables.keySet());
        rightComboBox.setOnAction(e -> {
            rightTableArea.getChildren().setAll(userTables
                    .get(rightComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });



        BorderPane.setMargin(left, new Insets(0, 10, 0, 0));

        leftTableArea.getChildren().setAll(allTable.tableViewGenerator.getTableView());
        topRightHBox.getChildren().addAll(rightComboBox);

        leftComboBox = new JFXComboBox<>();
        leftComboBox.setVisible(false);
        topLeftHBox.getChildren().addAll(leftComboBox);

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

        tableView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (event.getDragboard().hasString()) {
                Item item = allItems.get(Long.valueOf(db.getString()));
                if (!tableView.getItems().contains(item)) {
                    if (userTables.get(item.getOwner()).getTableView() == tableView) {
                        getPopupFactory().toast(5, "Do not drag things to oneself! This won't form a trade my friend");
                        return;
                    }
                    tableView.getItems().addAll(item);
                    if (tableView != allTable.getTableView()) {
                        System.out.println("Removing");
                        allTable.getTableView().getItems().remove(item);
                        allTable.getTableView().refresh();
                        System.out.println(allTable.getTableView().getItems().size());
                    }
                    tableView.refresh();
                }
                success = true;
            }
            event.setDropCompleted(success);
            event.consume();
        });

    }

    /*
    private Map<User, UserTable> getUserTables() {
        Map<User, UserTable> targetTables = new HashMap<>();
        Collection<Item> userItems = new ItemFilter(getAccountManager().getLoggedInUser()).getTradableItems();
        for (Item item : selectedItems) {
            if (!targetTables.containsKey(item.getOwner())) {
                UserTable userTable = new UserTable(item.getOwner());
                targetTables.put(item.getOwner(), userTable);
                userTable.addItem(item);
            } else {
                targetTables.get(item.getOwner()).addItem(item);
            }
        }
        UserTable currUserTable = new UserTable(getAccountManager().getLoggedInUser());
        currUserTable.addItem(userItems);
        targetTables.put(getAccountManager().getLoggedInUser(), currUserTable);
        targetTables.values().forEach(UserTable::build);
        return targetTables;
    }
     */

    private ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new UserStringConverter(comboBox));
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(users);
        return comboBox;
    }
}

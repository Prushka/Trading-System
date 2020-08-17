package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
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

    class UserTable {
        ObservableList<Item> items = FXCollections.observableArrayList();
        User owner;
        TableViewGenerator<Item> tableViewGenerator;
        TargetUserTableController targetUserTableController;

        UserTable(User owner) {
            this.owner = owner;
        }

        void addItem(Item... item) {
            items.addAll(item);
        }

        void addItem(Collection<Item> items) {
            this.items.addAll(items);
        }

        void build() {
            tableViewGenerator = new TableViewGenerator<>(items);
            tableViewGenerator.getTableView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            tableViewGenerator.getTableView().setPrefWidth(650);
            targetUserTableController = new TargetUserTableController(getControllerResources(), tableViewGenerator);
        }
    }

    Map<User, Map<User, UserTable>> userTables = new HashMap<>();

    private ComboBox<User> rightComboBox, leftComboBox;

    public TradeController(ControllerResources controllerResources, ObservableList<Item> selectedItems) {
        super(controllerResources);
        this.selectedItems = selectedItems;
    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        for (Item item : selectedItems) {
            if (!userTables.containsKey(item.getOwner())) {
                UserTable userTable = new UserTable(item.getOwner());
                userTables.put(item.getOwner(), getUserTables());
                userTable.addItem(item);
            }
        }

        Label initiatorLabel = new Label("From");
        Label targetLabel = new Label("To");

        rightComboBox = getUserComboBox(userTables.keySet());
        leftComboBox = getUserComboBox(userTables.keySet());

        leftComboBox.setOnAction(e -> {
            leftTableArea.getChildren().setAll(userTables.get(leftComboBox.getSelectionModel().getSelectedItem())
                    .get(leftComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
            rightTableArea.getChildren().setAll(userTables.get(leftComboBox.getSelectionModel().getSelectedItem())
                    .get(rightComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });

        rightComboBox.setOnAction(e -> {
            rightTableArea.getChildren().setAll(userTables.get(leftComboBox.getSelectionModel().getSelectedItem())
                    .get(rightComboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });

        BorderPane.setMargin(left, new Insets(0, 10, 0, 0));
        topLeftHBox.getChildren().addAll(initiatorLabel, leftComboBox);
        topRightHBox.getChildren().addAll(targetLabel, rightComboBox);
    }

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

    private ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new UserStringConverter(comboBox));
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(users);
        return comboBox;
    }
}

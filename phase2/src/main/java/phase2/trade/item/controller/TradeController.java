package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableView;
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
            tableViewGenerator.getTableView().setPrefWidth(600);
            targetUserTableController = new TargetUserTableController(getControllerResources(), tableViewGenerator);
        }
    }

    private final Map<User, UserTable> targetTables = new HashMap<>();

    public TradeController(ControllerResources controllerResources, ObservableList<Item> selectedItems) {
        super(controllerResources);
        this.selectedItems = selectedItems;
    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Collection<Item> userItems = new ItemFilter(getAccountManager().getLoggedInUser()).getTradableItems();

        System.out.println(userItems.size());
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

        ComboBox<User> rightComboBox = getUserComboBox(rightTableArea);
        //ComboBox<User> leftComboBox = getUserComboBox(leftTableArea);

        rightComboBox.getSelectionModel().select(0);
        //leftComboBox.getSelectionModel().select(0);

        BorderPane.setMargin(left, new Insets(0, 10, 0, 0));
        //topLeftHBox.getChildren().addAll(leftComboBox);
        topRightHBox.getChildren().addAll(rightComboBox);
    }

    private ComboBox<User> getUserComboBox(Pane tableArea) {
        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new UserStringConverter(comboBox));
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(targetTables.keySet());

        comboBox.setOnAction(event -> {
            tableArea.getChildren().setAll(targetTables.get(comboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });
        return comboBox;
    }
}

package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import phase2.trade.item.Item;
import phase2.trade.user.User;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ControllerProperty(viewFile = "trade.fxml")
public class TradeController extends AbstractController implements Initializable {

    @FXML
    private BorderPane root;

    private final ObservableList<Item> selectedItems;

    @FXML
    private HBox topLeftHBox, topRightHBox;

    @FXML
    private Pane leftTableArea, rightTableArea;

    class UserTable {
        ObservableList<Item> items = FXCollections.observableArrayList();
        User owner;
        TableViewGenerator<Item> tableViewGenerator;
        TargetUserTableController targetUserTableController;

        UserTable(User owner) {
            this.owner = owner;
        }

        void addItem(Item item) {
            items.add(item);
        }

        void build() {
            tableViewGenerator = new TableViewGenerator<>(items);
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

        for (Item item : selectedItems) {
            if (!targetTables.containsKey(item.getOwner())) {
                UserTable userTable = new UserTable(item.getOwner());
                targetTables.put(item.getOwner(), userTable);
                userTable.addItem(item);
            } else {
                targetTables.get(item.getOwner()).addItem(item);
            }
        }
        targetTables.values().forEach(UserTable::build);

        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new StringConverter<User>() {

            @Override
            public String toString(User object) {
                return object.getName();
            }

            @Override
            public User fromString(String string) {
                return comboBox.getItems().stream().filter(ap ->
                        ap.getName().equals(string)).findFirst().orElse(null);
            }
        });
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(targetTables.keySet());
        comboBox.setOnAction(event -> {
            rightTableArea.getChildren().setAll(targetTables.get(comboBox.getSelectionModel().getSelectedItem()).tableViewGenerator.getTableView());
        });
        topRightHBox.getChildren().addAll(comboBox);
    }
}

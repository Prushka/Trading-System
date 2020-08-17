package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Item;
import phase2.trade.trade.TradeOrder;
import phase2.trade.user.User;
import phase2.trade.view.window.AddressAlertController;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "trade.fxml")
public class TradeDetailController extends AbstractController implements Initializable {

    @FXML
    private BorderPane root;

    @FXML
    private HBox topLeftHBox, topRightHBox;

    @FXML
    private Pane leftTableArea, rightTableArea;

    @FXML
    private VBox left, right;

    @FXML
    private HBox buttonPane;

    private ComboBox<User> rightComboBox, leftComboBox;

    private final Map<User, Collection<Item>> userToItemToGet;

    private final Map<User, Map<User, UserTable>> userTablesCombination = new HashMap<>();

    private final Collection<Item> allItems = new HashSet<>();

    private final CreateTrade createTrade;

    public TradeDetailController(ControllerResources controllerResources, Map<User, Collection<Item>> userToItemToGet) {
        super(controllerResources);
        this.userToItemToGet = userToItemToGet;

        userToItemToGet.values().forEach(allItems::addAll);

        createTrade = new CreateTrade(allItems);

    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createTrade.createOrder(userToItemToGet);

        for (Map.Entry<User, Collection<Item>> entry : userToItemToGet.entrySet()) {
            userTablesCombination.put(entry.getKey(), getUserTables(entry.getKey()));
        }


        rightComboBox = getUserComboBox(userToItemToGet.keySet());
        leftComboBox = getUserComboBox(userToItemToGet.keySet());
        leftComboBox.setOnAction(e -> {
            User leftSelected = leftComboBox.getSelectionModel().getSelectedItem();
            User rightSelected = rightComboBox.getSelectionModel().getSelectedItem();
            refreshTableArea(leftSelected, rightSelected);
            rightComboBox.setItems(getUsersBesides(leftSelected));
        });
        rightComboBox.setOnAction(e -> {
            User leftSelected = leftComboBox.getSelectionModel().getSelectedItem();
            User rightSelected = rightComboBox.getSelectionModel().getSelectedItem();
            refreshTableArea(leftSelected, rightSelected);
            leftComboBox.setItems(getUsersBesides(rightSelected));
        });

        topLeftHBox.getChildren().addAll(leftComboBox);
        topRightHBox.getChildren().addAll(rightComboBox);
    }

    class GroupedDetailViews {
        DatePicker datePicker = new JFXDatePicker();
        JFXTimePicker timePicker = new JFXTimePicker();
        Button addressButton = getNodeFactory().getDefaultFlatButton("Address");

        AddressAlertController addressAlertController = getControllerFactory().getController(AddressAlertController::new);

        GroupedDetailViews() {
            addressAlertController.setAddress(getAccountManager().getLoggedInUser().getAddressBook().cloneSelectedAddressWithoutDetail());
            addressButton.setOnAction(event -> addressAlertController.display());
        }

        void consume(Pane parent) {
            parent.getChildren().setAll(datePicker, timePicker, addressButton);
        }
    }

    Map<User, Map<User, GroupedDetailViews>> detailViewsMap = new HashMap<>();

    private GroupedDetailViews createDetailLayoutIfNotExist(User leftSelected, User rightSelected) {
        if (!detailViewsMap.containsKey(leftSelected)) {
            detailViewsMap.put(leftSelected, new HashMap<>());
        }
        if (!detailViewsMap.get(leftSelected).containsKey(rightSelected)) {
            detailViewsMap.get(leftSelected).put(rightSelected, new GroupedDetailViews());
        }
        return detailViewsMap.get(leftSelected).get(rightSelected);
    }

    private void refreshTableArea(User leftSelected, User rightSelected) {
        if (leftSelected == null || rightSelected == null) return;
        leftTableArea.getChildren().setAll(userTablesCombination.get(rightSelected)
                .get(leftSelected).tableViewGenerator.getTableView());
        rightTableArea.getChildren().setAll(userTablesCombination.get(leftSelected)
                .get(rightSelected).tableViewGenerator.getTableView());
        createDetailLayoutIfNotExist(leftSelected, rightSelected).consume(buttonPane);
    }

    private ObservableList<User> getUsersBesides(User user) {
        ObservableList<User> list = FXCollections.observableArrayList(userToItemToGet.keySet());
        list.remove(user);
        return list;
    }

    private Map<User, UserTable> getUserTables(User matchesWhom) {
        Map<User, UserTable> table = new HashMap<>();
        for (TradeOrder order : createTrade.getTrade().getOrders()) {
            if (order.getInitiator().getUser().getUid().equals(matchesWhom.getUid())) {
                UserTable userTable = new UserTable(order.getTarget().getUser(),
                        FXCollections.observableArrayList(order.getTarget().getTradeItemHolder().getSetOfItems()), getControllerResources());
                table.put(order.getTarget().getUser(), userTable);
            }
        }
        return table;
    }

    private ComboBox<User> getUserComboBox(Collection<User> users) {
        ComboBox<User> comboBox = new JFXComboBox<>();
        comboBox.setConverter(new UserStringConverter(comboBox));
        comboBox.setCellFactory(e -> new UserCell());
        comboBox.getItems().addAll(users);
        return comboBox;
    }
}

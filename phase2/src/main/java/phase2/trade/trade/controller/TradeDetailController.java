package phase2.trade.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.item.controller.CartController;
import phase2.trade.item.controller.CreateTrade;
import phase2.trade.item.controller.UserCell;
import phase2.trade.item.controller.UserStringConverter;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.command.CreateTradeCommand;
import phase2.trade.user.User;
import phase2.trade.widget.TradeAddressWidget;
import phase2.trade.widget.TradeOptionWidget;
import phase2.trade.widget.TradeTimeWidget;

import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

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
    private HBox buttonPane, globalPane;

    private ComboBox<User> rightComboBox, leftComboBox;

    private final Map<User, Collection<Item>> usersToItemsToGet;

    private final Map<User, Map<User, UserTable>> userTablesCombination = new HashMap<>();

    private final CreateTrade createTrade;

    private final Map<TradeOrder, WidgetBundle> widgetBundleMap = new HashMap<>();

    public TradeDetailController(ControllerResources controllerResources, Map<User, Collection<Item>> usersToItemsToGet) {
        super(controllerResources);
        this.usersToItemsToGet = usersToItemsToGet;

        createTrade = new CreateTrade(usersToItemsToGet);

    }

    //dashboard-table-view
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        createTrade.createOrder();

        for (Map.Entry<User, Collection<Item>> entry : usersToItemsToGet.entrySet()) {
            userTablesCombination.put(entry.getKey(), getUserTables(entry.getKey()));
        }


        rightComboBox = getUserComboBox(createTrade.getUsersInvolved());
        leftComboBox = getUserComboBox(createTrade.getUsersInvolved());
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

        Label offersFollowingItemsA = new Label("  offers following items");
        Label offersFollowingItemsB = new Label("  offers following items");
        topLeftHBox.getChildren().addAll(leftComboBox, offersFollowingItemsA);
        topRightHBox.getChildren().addAll(rightComboBox, offersFollowingItemsB);

        TradeOptionWidget tradeOptionWidget = getControllerFactory().getController(TradeOptionWidget::new);


        EventHandler<ActionEvent> handler = e -> {
            CreateTradeCommand createTradeCommand = getCommandFactory().getCommand(
                    CreateTradeCommand::new, c -> c.setToUpdate(createTrade.getTrade()));
            for (Map.Entry<TradeOrder, WidgetBundle> entry : widgetBundleMap.entrySet()) {
                entry.getKey().setAddressTrade(entry.getValue().tradeAddressWidget.getSubmittedAddress());
                entry.getKey().setDateAndTime(entry.getValue().tradeTimeWidget.getTime());
            }
            createTradeCommand.execute((result, status) -> {
                status.setSucceeded(() -> {
                    getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(CartController::new));
                    getNotificationFactory().toast("Order successfully placed!");
                });
                status.handle(getNotificationFactory());
            });
        };
        tradeOptionWidget.setEventHandler(handler);
        globalPane.getChildren().addAll(getSceneManager().loadPane(tradeOptionWidget));
    }

    class WidgetBundle {
        TradeTimeWidget tradeTimeWidget;
        TradeAddressWidget tradeAddressWidget;

        public WidgetBundle(User leftSelected, User rightSelected) {
            tradeTimeWidget = new TradeTimeWidget(getControllerResources(), leftSelected, rightSelected);
            tradeAddressWidget = new TradeAddressWidget(getControllerResources(), leftSelected, rightSelected);
        }

        void consume(Pane pane) {
            pane.getChildren().setAll(getSceneManager().loadPane(tradeTimeWidget), getSceneManager().loadPane(tradeAddressWidget));
        }
    }

    private WidgetBundle createWidgetBundleIfNotExist(User leftSelected, User rightSelected) {
        TradeOrder order = findOrderByUserPair(leftSelected, rightSelected);
        if (order == null) return null;
        if (!widgetBundleMap.containsKey(order)) {
            widgetBundleMap.put(order, new WidgetBundle(leftSelected, rightSelected));
        }
        return widgetBundleMap.get(order);
    }

    private TradeOrder findOrderByUserPair(User a, User b) {
        return createTrade.getTrade().findOrderByUserPair(a, b);
    }

    private void refreshTableArea(User leftSelected, User rightSelected) {
        if (leftSelected == null || rightSelected == null) return;
        leftTableArea.getChildren().setAll(userTablesCombination.get(rightSelected)
                .get(leftSelected).tableViewGenerator.getTableView());
        rightTableArea.getChildren().setAll(userTablesCombination.get(leftSelected)
                .get(rightSelected).tableViewGenerator.getTableView());
        WidgetBundle widgetBundle = createWidgetBundleIfNotExist(leftSelected, rightSelected);
        if (widgetBundle != null) widgetBundle.consume(buttonPane);
    }

    private ObservableList<User> getUsersBesides(User user) {
        ObservableList<User> list = FXCollections.observableArrayList(createTrade.getUsersInvolved());
        list.remove(user);
        return list;
    }

    // well these loops need optimization
    private Map<User, UserTable> getUserTables(User matchesWhom) {
        Map<User, UserTable> table = new HashMap<>();
        for (TradeOrder order : createTrade.getTrade().getOrders()) {
            if (order.getLeftUser().getUid().equals(matchesWhom.getUid())) {
                UserTable userTable = new UserTable(order.getRightBundle().getUser(),
                        FXCollections.observableArrayList(
                                order.getRightBundle().getTradeItemHolder().getSetOfItems()), getControllerResources());
                table.put(order.getRightBundle().getUser(), userTable);
            }
            if (order.getRightUser().getUid().equals(matchesWhom.getUid())) {
                UserTable userTable = new UserTable(order.getRightBundle().getUser(),
                        FXCollections.observableArrayList(
                                order.getLeftBundle().getTradeItemHolder().getSetOfItems()), getControllerResources());
                table.put(order.getLeftUser(), userTable);
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

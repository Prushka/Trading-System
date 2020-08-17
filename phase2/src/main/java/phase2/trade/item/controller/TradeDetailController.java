package phase2.trade.item.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.item.Item;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.command.CreateTradeCommand;
import phase2.trade.user.User;
import phase2.trade.view.widget.TradeAddressWidget;
import phase2.trade.view.widget.TradeTimeWidget;

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

    private final CreateTrade createTrade;

    public TradeDetailController(ControllerResources controllerResources, Map<User, Collection<Item>> usersToItemsToGet) {
        super(controllerResources);
        this.userToItemToGet = usersToItemsToGet;

        createTrade = new CreateTrade(usersToItemsToGet);

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

        Label offersFollowingItems = new Label("  offers following items");
        topLeftHBox.getChildren().addAll(leftComboBox, offersFollowingItems);
        topRightHBox.getChildren().addAll(rightComboBox, offersFollowingItems);
        Button finishTrade = getNodeFactory().getDefaultFlatButton("Finish Trade");
        getPane(DashboardPane.TOP).getChildren().setAll(finishTrade);
        finishTrade.setOnAction(e -> {
            CreateTradeCommand createTradeCommand = getCommandFactory().getCommand(CreateTradeCommand::new, c -> c.setToUpdate(createTrade.getTrade()));
            createTradeCommand.execute(new ResultStatusCallback<Trade>() {
                @Override
                public void call(Trade result, ResultStatus status) {

                }
            });
        });
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

    Map<User, Map<User, WidgetBundle>> detailViewsMap = new HashMap<>();

    private WidgetBundle createDetailLayoutIfNotExist(User leftSelected, User rightSelected) {
        if (!detailViewsMap.containsKey(leftSelected)) {
            detailViewsMap.put(leftSelected, new HashMap<>());
        }
        if (!detailViewsMap.get(leftSelected).containsKey(rightSelected)) {
            detailViewsMap.get(leftSelected).put(rightSelected, new WidgetBundle(leftSelected, rightSelected));
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

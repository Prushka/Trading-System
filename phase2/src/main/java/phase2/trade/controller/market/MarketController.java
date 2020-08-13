package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.item.Item;
import phase2.trade.presenter.GeneralTableViewController;
import phase2.trade.trade.Trade;
import phase2.trade.trade.command.ConfirmTrade;
import phase2.trade.trade.command.CreateTrade;
import phase2.trade.trade.command.EditTrade;
import phase2.trade.trade.command.TradeCommand;
import phase2.trade.user.User;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketController extends GeneralTableViewController implements Initializable {

    private TradeCommand tc, edit, confirm;

    @FXML
    private VBox root;
    private TabPane tradeTabs;
    private Tab tradeView;
    private Tab tradeMarket;
    private TableView<Trade> trades;
    private TableColumn<Trade, String> statusColumn;
    private ObservableList<Trade> tradesList;
    private JFXButton editButton;
    private JFXButton confirmButton;
    private VBox content2;
    private HBox buttonBar;
    private HBox users;
    private HBox meeting;
    private VBox content;
    private HBox dateTime;
    private Label traders;
    private JFXComboBox<User> user1;
    private JFXComboBox<User> user2;
    private JFXComboBox<User> user3;
    private HBox items;
    private JFXButton addItems2;
    private ListView<Pane> itemList2;
    private JFXButton addItems3;
    private ListView<Pane> itemList3;
    private Label date;
    private JFXTextField year;
    private JFXTextField month;
    private JFXTextField day;
    private JFXTextField hour;
    private JFXTextField minute;
    private HBox place;
    private Label tradeLocation;
    private JFXTextField country;
    private JFXTextField city;
    private JFXTextField street;
    private JFXTextField streetNum;
    private HBox type;
    private Label tradeType;
    private JFXButton tradeButton;
    private JFXComboBox<String> isPermanent;

    public MarketController(ControllerResources controllerResources){
        super(controllerResources, false, false);
        tc = getCommandFactory().getCommand(CreateTrade::new);
        edit = getCommandFactory().getCommand(EditTrade::new);
        confirm = getCommandFactory().getCommand(ConfirmTrade::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meeting = new HBox();
        content = new VBox();
        content2 = new VBox();
        buttonBar = new HBox();
        tradeTabs = new TabPane();
        tradeTabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tradeView = new Tab("My Trades");
        tradeMarket = new Tab("Make a Trade");

        // Trades
        statusColumn = new TableColumn<>("Status: ");
        statusColumn.setMinWidth(200);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("tradeStatus"));
        trades = new TableView<>();
        tradesList = FXCollections.observableArrayList();
        // tradesList.addAll(getControllerResources().getGatewayBundle().getEntityBundle().getTradeGateway()
           //     .findByUser(getControllerResources().getAccountManager().getLoggedInUser().getUid()));
        trades.setItems(tradesList);
        trades.getColumns().addAll(statusColumn);
        editButton = new JFXButton("Edit Trade");
        editButton.setOnAction(e -> editButtonClicked());
        confirmButton = new JFXButton("Confirm Button");
        confirmButton.setOnAction(e -> confirmButtonClicked());
        buttonBar.getChildren().addAll(editButton, confirmButton);
        buttonBar.setSpacing(20);
        buttonBar.setAlignment(Pos.BOTTOM_LEFT);
        buttonBar.setPadding(new Insets(5, 10, 5, 10));

        //Users
        users = new HBox();
        users.setSpacing(20);
        users.setAlignment(Pos.BOTTOM_LEFT);
        users.setPadding(new Insets(5, 10, 5, 10));
        traders = new Label("Other Traders: ");
        user2 = new JFXComboBox<>();
        // user2.getItems().addAll(gatewayBundle.getEntityBundle().getUserGateway().findAll());
        user3 = new JFXComboBox<>();
        users.getChildren().addAll(traders, user2, user3);

        // Items
        items = new HBox();
        users.setSpacing(20);
        users.setAlignment(Pos.BOTTOM_LEFT);
        users.setPadding(new Insets(5, 10, 5, 10));
        addItems2 = new JFXButton("ADD ITEMS FOR TRADER 1");
        itemList2 = new ListView<>();
        addItems3 = new JFXButton("ADD ITEMS FOR TRADER 2");
        itemList3 = new ListView<>();
        items.getChildren().addAll(itemList2, addItems2, itemList3, addItems3);

        // Date
        dateTime = new HBox();
        dateTime.setSpacing(20);
        dateTime.setAlignment(Pos.BOTTOM_LEFT);
        dateTime.setPadding(new Insets(5, 10, 5, 10));
        date = new Label("Date: ");
        year = new JFXTextField();
        year.setPromptText("YEAR");
        month = new JFXTextField();
        month.setPromptText("MONTH");
        day = new JFXTextField();
        day.setPromptText("DAY");
        hour = new JFXTextField();
        hour.setPromptText("HOUR");
        minute = new JFXTextField();
        minute.setPromptText("MINUTE");
        dateTime.getChildren().addAll(date, year, month, day, hour, minute);

        // Place
        place = new HBox();
        place.setSpacing(20);
        place.setAlignment(Pos.BOTTOM_LEFT);
        place.setPadding(new Insets(5, 10, 5, 10));
        tradeLocation = new Label("Location: ");
        street = new JFXTextField();
        street.setPromptText("STREET");
        streetNum = new JFXTextField();
        streetNum.setPromptText("STREET NUMBER");
        city = new JFXTextField();
        city.setPromptText("CITY");
        country = new JFXTextField();
        country.setPromptText("COUNTRY");
        place.getChildren().addAll(tradeLocation, street, streetNum, city, country);

        // Type
        type = new HBox();
        type.setSpacing(20);
        type.setAlignment(Pos.BOTTOM_LEFT);
        type.setPadding(new Insets(5, 10, 5, 10));
        tradeType = new Label("Type of Trade: ");
        isPermanent = new JFXComboBox<>();
        isPermanent.getItems().addAll("PERMANENT", "TEMPORARY");
        isPermanent.getSelectionModel().selectFirst();
        type.getChildren().addAll(tradeType, isPermanent);

        // Trading
        tradeButton = new JFXButton("Submit Trade");
        tradeButton.setOnAction(e -> tradeButtonClicked());

        content2.getChildren().addAll(trades, buttonBar);
        tradeView.setContent(content2);
        content.getChildren().addAll(users, items, dateTime, place, type, tradeButton);
        tradeMarket.setContent(content);
        tradeTabs.getTabs().setAll(tradeView, tradeMarket);
        meeting.getChildren().addAll(tradeTabs);
        root.getChildren().add(meeting);
    }

    public void editButtonClicked(){
        Trade currTrade = trades.getSelectionModel().getSelectedItem();
        edit.setUid(currTrade.getUid());
    }

    public void confirmButtonClicked(){
        Trade currTrade = trades.getSelectionModel().getSelectedItem();
        confirm.setUid(currTrade.getUid());
    }


    public void tradeButtonClicked(){
        List<User> allUsers = new ArrayList<>();
        List<List<Item>> allItems = new ArrayList<>();
        if (isPermanent.getSelectionModel().getSelectedItem().equals("PERMANENT")){
            tc.execute(new ResultStatusCallback() { @Override
                       public void call(Object result, ResultStatus resultStatus) {
                           System.out.println("success");
                       }
                       }, year.getText(), month.getText(),
                    day.getText(), hour.getText(), minute.getText(), country.getText(), city.getText(), street.getText(),
                    streetNum.getText(), "true");
        } else if (isPermanent.getSelectionModel().getSelectedItem().equals("TEMPORARY")){
            tc.execute(new ResultStatusCallback() { @Override
                       public void call(Object result, ResultStatus resultStatus) {
                           System.out.println("success");
                       }
                       }, year.getText(), month.getText(),
                    day.getText(), hour.getText(), minute.getText(), country.getText(), city.getText(), street.getText(),
                    streetNum.getText(), "false");
        }
    }
}

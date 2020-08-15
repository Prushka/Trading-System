package phase2.trade.controller.market;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.AbstractTableViewController;
import phase2.trade.item.Item;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.trade.Trade;
import phase2.trade.trade.command.ConfirmTrade;
import phase2.trade.trade.command.CreateTrade;
import phase2.trade.trade.command.EditTrade;
import phase2.trade.trade.command.TradeCommand;
import phase2.trade.user.User;
import phase2.trade.view.ListViewGenerator;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "market.fxml")
public class MarketController extends AbstractTableViewController implements Initializable {

    private TradeCommand tc, edit, confirm;

    @FXML
    private VBox root, content, content2;
    private HBox buttonBar, users, meeting, dateTime, items, place, type;
    private Label traders, date, tradeLocation, tradeType;
    private ListView<Pane> itemList2, itemList3;
    private TabPane tradeTabs;
    private Tab tradeView, tradeMarket;
    private TableView<Trade> trades;
    private TableColumn<Trade, String> statusColumn;
    private ObservableList<Trade> tradesList;
    private JFXButton editDateTimeButton, editLocationButton, confirmButton, addItems2, addItems3, tradeButton;
    private JFXComboBox<User> user2, user3;
    private JFXComboBox<String> isPermanent;
    private JFXTextField year, month, day, hour, minute, country, city, street, streetNum;

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
        //tradesList = FXCollections.observableArrayList(getGatewayBundle().getEntityBundle().getTradeGateway()
          //      .findByUser(getControllerResources().getAccountManager().getLoggedInUser().getUid()));
        trades.setItems(tradesList);
        trades.getColumns().addAll(statusColumn);
        editDateTimeButton = new JFXButton("Edit Date and Time");
        editDateTimeButton.setOnAction(e -> editDateTimeClicked());
        editLocationButton = new JFXButton("Edit Location");
        editLocationButton.setOnAction(e -> editLocationClicked());
        confirmButton = new JFXButton("Confirm Button");
        confirmButton.setOnAction(e -> confirmButtonClicked());
        buttonBar.getChildren().addAll(editDateTimeButton, editLocationButton, confirmButton);
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
        // user2.getItems().addAll(getGatewayBundle().getEntityBundle().getUserGateway().findAll());
        user3 = new JFXComboBox<>();
        users.getChildren().addAll(traders, user2, user3);

        // Items
        items = new HBox();
        users.setSpacing(20);
        users.setAlignment(Pos.BOTTOM_LEFT);
        users.setPadding(new Insets(5, 10, 5, 10));
        addItems2 = new JFXButton("ADD ITEMS FOR TRADER 2");
        addItems2.setOnAction(e -> items1Clicked());
        itemList2 = new ListView<>();
        addItems3 = new JFXButton("ADD ITEMS FOR TRADER 3");
        addItems3.setOnAction(e -> items2Clicked());
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

    public void items1Clicked(){
        GeneralVBoxAlert popup = getPopupFactory().vBoxAlert("Available Items for Trader 2", "");
        ListViewGenerator<Item> availableItems = new ListViewGenerator<>(new JFXListView<>());
        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                for (Item item : result) {
                    availableItems.addElement(item);
                }
            });
            resultStatus.handle(getPopupFactory());
        });
        popup.addNodes(availableItems.build());
        popup.display();
    }

    public void items2Clicked(){
        GeneralVBoxAlert popup = getPopupFactory().vBoxAlert("Available Items for Trader 3", "");
        ListViewGenerator<Item> availableItems = new ListViewGenerator<>(new JFXListView<>());
        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                for (Item item : result) {
                    availableItems.addElement(item);
                }
            });
            resultStatus.handle(getPopupFactory());
        });
        popup.addNodes(availableItems.build());
        popup.display();
    }

    public void editDateTimeClicked(){
        TextField newYear = new TextField();
        newYear.setPromptText("YEAR");
        TextField newMonth = new TextField();
        newMonth.setPromptText("MONTH");
        TextField newDay = new TextField();
        newDay.setPromptText("DAY");
        TextField newHour = new TextField();
        newHour.setPromptText("HOUR");
        TextField newMinute = new TextField();
        newMinute.setPromptText("MINUTE");

        GeneralVBoxAlert popup = getPopupFactory().vBoxAlert("Edit Trade Date and Time", "");
        popup.setEventHandler(event -> {
            Trade currTrade = trades.getSelectionModel().getSelectedItem();
            edit.setUid(currTrade.getUid());
            edit.execute(((result, status) -> {
                status.setFailed(() -> getPopupFactory().toast(5, "Not proper format"));
                status.handle(getPopupFactory());
            }), newYear.getText(), newMonth.getText(),
                    newDay.getText(), newHour.getText(), newMinute.getText());
        });

        popup.addNodes(newYear, newMonth, newDay, newHour, newMinute);
        popup.display();
    }

    public void editLocationClicked(){
        TextField newCountry = new TextField();
        newCountry.setPromptText("COUNTRY");
        TextField newCity = new TextField();
        newCity.setPromptText("CITY");
        TextField newStreet = new TextField();
        newStreet.setPromptText("STREET");
        TextField newStreetNum = new TextField();
        newStreetNum.setPromptText("STREET NUMBER");

        GeneralVBoxAlert popup = getPopupFactory().vBoxAlert("Edit Trade Location", "");
        popup.setEventHandler(event -> {
            Trade currTrade = trades.getSelectionModel().getSelectedItem();
            edit.setUid(currTrade.getUid());
            edit.execute(((result, status) -> {
                        status.setFailed(() -> getPopupFactory().toast(5, "Not proper format"));
                        status.handle(getPopupFactory());
                    }), newCountry.getText(), newCity.getText(), newStreet.getText(),
                    newStreetNum.getText(), "false");
        });

        popup.addNodes(newCountry, newCity, newStreet, newStreetNum);
        popup.display();
    }

    public void confirmButtonClicked(){
        Trade currTrade = trades.getSelectionModel().getSelectedItem();
        confirm.setUid(currTrade.getUid());
        confirm.execute(new ResultStatusCallback() { @Override
        public void call(Object result, ResultStatus resultStatus) {
            System.out.println("success");
        }
        });
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
        year.clear();
        month.clear();
        day.clear();
        hour.clear();
        minute.clear();
        country.clear();
        city.clear();
        street.clear();
        streetNum.clear();
        // System.out.println(getGatewayBundle().getEntityBundle().getTradeGateway().findAll());
    }
}

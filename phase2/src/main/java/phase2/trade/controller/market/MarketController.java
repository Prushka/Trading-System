package phase2.trade.controller.market;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.controller.AbstractListController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.controller.CartController;
import phase2.trade.trade.MeetUpOrder;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;
import phase2.trade.trade.command.ConfirmTrade;
import phase2.trade.trade.command.CreateTrade;
import phase2.trade.trade.command.EditTrade;
import phase2.trade.trade.command.TradeCommand;
import phase2.trade.user.User;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@ControllerProperty(viewFile = "market.fxml")
public class MarketController extends AbstractListController<Trade> implements Initializable {

    private TradeCommand edit, confirm;
    private CreateTrade tc;

    @FXML
    private VBox root, content, content2;
    private HBox buttonBar, users, meeting, dateTime, items, place, type;
    private Label date, tradeLocation, tradeType;
    private TabPane tradeTabs;
    private Tab tradeView, tradeMarket;
    private TableView<Trade> trades;
    private TableColumn<Trade, Long> idColumn;
    private TableColumn<Trade, String> statusColumn, dateColumn, locationColumn;
    private TableColumn<Trade, Boolean> typeColumn, confirmColumn;
    private ObservableList<Trade> tradesList;
    private JFXButton editDateTimeButton, editLocationButton, confirmButton, tradeButton, addTraderButton;
    private JFXComboBox<String> isPermanent;
    private JFXTextField year, month, day, hour, minute, country, city, street, streetNum;

    public MarketController(ControllerResources controllerResources){
        super(controllerResources, false);
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
        idColumn = new TableColumn<>("Trade ID: ");
        idColumn.setMinWidth(200);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("uid"));
        dateColumn = new TableColumn<>("Date: ");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOrder()
                .getDateAndTime().toString()));
        locationColumn = new TableColumn<>("Location: ");
        locationColumn.setMinWidth(200);
        locationColumn.setCellValueFactory(cellData -> new SimpleStringProperty(((MeetUpOrder) cellData.getValue()
                .getOrder()).getLocation().getCity() + ", " + ((MeetUpOrder) cellData.getValue()
                .getOrder()).getLocation().getCountry()));
        statusColumn = new TableColumn<>("Status: ");
        statusColumn.setMinWidth(200);
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTradeState().toString()));
        confirmColumn = new TableColumn<>("Confirmation: ");
        confirmColumn.setMinWidth(200);
        confirmColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getOrder()
                .getTraders().get(0).getConfirmations()));
        typeColumn = new TableColumn<>("Permanency: ");
        typeColumn.setMinWidth(200);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("isPermanent"));
        trades = new TableView<>();
        trades.setItems(tradesList);
        trades.getColumns().addAll(idColumn, dateColumn, locationColumn, statusColumn, typeColumn);
        editDateTimeButton = new JFXButton("Edit Date and Time");
        editDateTimeButton.setOnAction(e -> editDateTimeClicked());
        editLocationButton = new JFXButton("Edit Location");
        editLocationButton.setOnAction(e -> editLocationClicked());
        confirmButton = new JFXButton("Confirm Button");
        confirmButton.setOnAction(e -> confirmButtonClicked());
        buttonBar.getChildren().addAll(confirmButton, editDateTimeButton, editLocationButton);
        buttonBar.setSpacing(20);
        buttonBar.setAlignment(Pos.BOTTOM_LEFT);
        buttonBar.setPadding(new Insets(5, 10, 5, 10));

        //Users
        users = new HBox();
        users.setSpacing(20);
        users.setAlignment(Pos.BOTTOM_LEFT);
        users.setPadding(new Insets(5, 10, 5, 10));
        addTraderButton = new JFXButton("Add Trader");
        addTraderButton.setOnAction(e -> addTraderClicked());
        users.getChildren().addAll(addTraderButton);

        // Items
        items = new HBox();
        items.setSpacing(20);
        items.setAlignment(Pos.BOTTOM_LEFT);
        items.setPadding(new Insets(5, 10, 5, 10));
        items.getChildren().addAll(getSceneManager().loadPane(new CartController(getControllerResources(), ItemListType.CART)));

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

        getGatewayBundle().getEntityBundle().getTradeGateway().submitSession((gateway) -> {
            List<Trade> matchedTrades = gateway.findAll();
            tradesList = FXCollections.observableArrayList(matchedTrades);
            trades.setItems(tradesList);
        });
    }

    public void addTraderClicked(){
        Parent itemList = getSceneManager().loadPane(new CartController(getControllerResources(), ItemListType.CART));
        items.getChildren().add(itemList);
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
            edit.setTradeId(currTrade.getUid());
            edit.execute(((result, status) -> {
                status.setFailed(() -> getPopupFactory().toast(5, "Not proper format"));
                status.handle(getPopupFactory());
            }), newYear.getText(), newMonth.getText(),
                    newDay.getText(), newHour.getText(), newMinute.getText());
        });

        popup.addNodes(newYear, newMonth, newDay, newHour, newMinute);
        popup.display();
        reloadTable();
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
            edit.setTradeId(currTrade.getUid());
            edit.execute(((result, status) -> {
                        status.setFailed(() -> getPopupFactory().toast(5, "Not proper format"));
                        status.handle(getPopupFactory());
                    }), newCountry.getText(), newCity.getText(), newStreet.getText(),
                    newStreetNum.getText(), "false");
        });

        popup.addNodes(newCountry, newCity, newStreet, newStreetNum);
        popup.display();
        reloadTable();
    }

    public void confirmButtonClicked(){
        Trade currTrade = trades.getSelectionModel().getSelectedItem();
        confirm.setTradeId(currTrade.getUid());
        confirm.execute(new ResultStatusCallback() { @Override
        public void call(Object result, ResultStatus resultStatus) {
            System.out.println("success");
        }
        });
        reloadTable();
    }


    public void tradeButtonClicked(){
        List<User> allUsers = new ArrayList<>(); // TODO: replace with selected combo boxes
        allUsers.add(getAccountManager().getLoggedInUser());
        List<Set<Item>> allItems = new ArrayList<>(); // TODO: replace with selected combo boxes
        tc.setTraders(allUsers);
        tc.setTraderItems(allItems);
        if (isPermanent.getSelectionModel().getSelectedItem().equals("PERMANENT") && validate()){
            tc.execute(new ResultStatusCallback() { @Override
                       public void call(Object result, ResultStatus resultStatus) {
                           System.out.println("success");
                       }
                       }, year.getText(), month.getText(),
                    day.getText(), hour.getText(), minute.getText(), country.getText(), city.getText(), street.getText(),
                    streetNum.getText(), "true");
        } else if (isPermanent.getSelectionModel().getSelectedItem().equals("TEMPORARY") && validate()){
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
        reloadTable();
    }

    public boolean validate() {
        try {
            return (LocalDateTime.of(Integer.parseInt(year.getText()), Integer.parseInt(month.getText()),
                    Integer.parseInt(day.getText()), Integer.parseInt(hour.getText()), Integer.parseInt(minute.getText())))
                    .isAfter(LocalDateTime.now());
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }
    }

    public void reloadTable(){
        getGatewayBundle().getEntityBundle().getTradeGateway().submitSession((gateway) -> {
            List<Trade> matchedTrades = gateway.findAll();
            tradesList = FXCollections.observableArrayList(matchedTrades);
            trades.getItems().clear();
            trades.setItems(tradesList);
        });
    }
}

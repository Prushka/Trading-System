package phase2.trade.controller.market;

import javafx.fxml.Initializable;
import phase2.trade.controller.AbstractListController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.trade.Trade;

@ControllerProperty(viewFile = "market.fxml")
public class MarketController extends AbstractListController<Trade> implements Initializable {
    public MarketController(ControllerResources controllerResources, boolean ifMultipleSelection) {
        super(controllerResources, ifMultipleSelection);
    }

    /*
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
    private TableColumn<Trade, Integer> editsColumn;
    private TableColumn<Trade, Boolean> typeColumn, confirmColumn;
    private ObservableList<Trade> tradesList;
    private JFXButton editDateTimeButton, editLocationButton, confirmButton, tradeButton, addTraderButton;
    private JFXComboBox<String> isPermanent, year, month, day, hour, minute, country, city;

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
        editsColumn = new TableColumn<>("Edits: ");
        editsColumn.setMinWidth(200);
        editsColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getOrder().getTraders().get(0).getEdits()).asObject());
        confirmColumn = new TableColumn<>("Confirmation: ");
        confirmColumn.setMinWidth(200);
        confirmColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().getOrder()
                .getTraders().get(0).getConfirmations()));
        typeColumn = new TableColumn<>("Permanency: ");
        typeColumn.setMinWidth(200);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("isPermanent"));
        trades = new TableView<>();
        trades.setItems(tradesList);
        trades.getColumns().addAll(idColumn, dateColumn, locationColumn, statusColumn, editsColumn, confirmColumn, typeColumn);
        // add editsColumn, confirmColumn -- need to remove previous trades because they include incomplete content
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
        addTraderClicked();
        addTraderClicked();

        // Date
        dateTime = new HBox();
        dateTime.setSpacing(20);
        dateTime.setAlignment(Pos.BOTTOM_LEFT);
        dateTime.setPadding(new Insets(5, 10, 5, 10));
        date = new Label("Date (YEAR/ MONTH /DAY /HOUR /MINUTE): ");
        year = new JFXComboBox<>();
        year.getItems().setAll("2020", "2021");
        month = new JFXComboBox<>();
        month.getItems().setAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12");
        day = new JFXComboBox<>();
        day.getItems().setAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
        hour = new JFXComboBox<>();
        hour.getItems().setAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23");
        minute = new JFXComboBox<>();
        minute.getItems().setAll("0", "30");
        dateTime.getChildren().addAll(date, year, month, day, hour, minute);

        // Place
        place = new HBox();
        place.setSpacing(20);
        place.setAlignment(Pos.BOTTOM_LEFT);
        place.setPadding(new Insets(5, 10, 5, 10));
        tradeLocation = new Label("Location: ");
        city = new JFXComboBox<>();
        city.getItems().setAll("Toronto");
        country = new JFXComboBox<>();
        country.getItems().setAll("Canada");
        place.getChildren().addAll(tradeLocation, city, country);

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
            List<Trade> matchedTrades = gateway.findByUser(getAccountManager().getLoggedInUser());
            tradesList = FXCollections.observableArrayList(matchedTrades);
            trades.setItems(tradesList);
        });
    }

    public void addTraderClicked(){
        Parent itemList = getSceneManager().loadPane(CartController::new);
        items.getChildren().add(itemList);
    }

    public void editDateTimeClicked(){
        Label yearText = new Label("Year: ");
        Label monthText = new Label("Month: ");
        Label dayText = new Label("Day: ");
        Label hourText = new Label("Hour: ");
        Label minuteText = new Label("Minute: ");

        JFXComboBox<String> newYear = new JFXComboBox<>();
        newYear.getItems().setAll("2020", "2021");
        JFXComboBox<String> newMonth = new JFXComboBox<>();
        newMonth.getItems().setAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10","11", "12");
        JFXComboBox<String> newDay = new JFXComboBox<>();
        newDay.getItems().setAll("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16",
                "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
        JFXComboBox<String> newHour = new JFXComboBox<>();
        newHour.getItems().setAll("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
                "16", "17", "18", "19", "20", "21", "22", "23");
        JFXComboBox<String> newMinute = new JFXComboBox<>();
        newMinute.getItems().setAll("0", "30");

        GeneralVBoxAlert popup = getNotificationFactory().vBoxAlert("Edit Trade Date and Time", "");
        popup.setEventHandler(event -> {
            Trade currTrade = trades.getSelectionModel().getSelectedItem();
            edit.setTradeId(currTrade.getUid());
            edit.execute(((result, status) -> {
                status.setFailed(() -> getNotificationFactory().toast(5, "Not proper format"));
                status.handle(getNotificationFactory());
            }), newYear.getSelectionModel().getSelectedItem(), newMonth.getSelectionModel().getSelectedItem(),
                    newDay.getSelectionModel().getSelectedItem(), newHour.getSelectionModel().getSelectedItem(),
                    newMinute.getSelectionModel().getSelectedItem());
        });

        popup.addNodes(yearText, newYear, monthText, newMonth, dayText, newDay, hourText, newHour, minuteText, newMinute);
        popup.display();
        reloadTable();
    }

    public void editLocationClicked(){
        Label countryText = new Label("Country: ");
        Label cityText = new Label("City: ");
        JFXComboBox<String> newCountry = new JFXComboBox();
        newCountry.getItems().setAll("Canada");
        JFXComboBox<String> newCity = new JFXComboBox();
        newCity.getItems().setAll("Toronto");

        GeneralVBoxAlert popup = getNotificationFactory().vBoxAlert("Edit Trade Location", "");
        popup.setEventHandler(event -> {
            Trade currTrade = trades.getSelectionModel().getSelectedItem();
            edit.setTradeId(currTrade.getUid());
            edit.execute(((result, status) -> {
                        status.setFailed(() -> getNotificationFactory().toast(5, "Not proper format"));
                        status.handle(getNotificationFactory());
                    }), newCountry.getSelectionModel().getSelectedItem(),
                    newCity.getSelectionModel().getSelectedItem(), "false");
        });

        popup.addNodes(countryText, newCountry, cityText, newCity);
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
        for (int i = 0; i < allUsers.size(); i++){
            Set<Item> itemList = new HashSet<>();
            allItems.add(itemList);
        }
        tc.setTraders(allUsers);
        tc.setTraderItems(allItems);
        if (isPermanent.getSelectionModel().getSelectedItem().equals("PERMANENT") && validate()){
            tc.execute(new ResultStatusCallback() { @Override
                       public void call(Object result, ResultStatus resultStatus) {
                           System.out.println("success");
                       }
                       }, year.getSelectionModel().getSelectedItem(), month.getSelectionModel().getSelectedItem(),
                    day.getSelectionModel().getSelectedItem(), hour.getSelectionModel().getSelectedItem(),
                    minute.getSelectionModel().getSelectedItem(), country.getSelectionModel().getSelectedItem(),
                    city.getSelectionModel().getSelectedItem(), "true");
        } else if (isPermanent.getSelectionModel().getSelectedItem().equals("TEMPORARY") && validate()){
            tc.execute(new ResultStatusCallback() { @Override
                       public void call(Object result, ResultStatus resultStatus) {
                           System.out.println("success");
                       }
                       }, year.getSelectionModel().getSelectedItem(), month.getSelectionModel().getSelectedItem(),
                    day.getSelectionModel().getSelectedItem(), hour.getSelectionModel().getSelectedItem(),
                    minute.getSelectionModel().getSelectedItem(), country.getSelectionModel().getSelectedItem(),
                    city.getSelectionModel().getSelectedItem(), "false");
        }
        reloadTable();
    }

    public boolean validate() {
        try {
            return (LocalDateTime.of(Integer.parseInt(year.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(month.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(day.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(hour.getSelectionModel().getSelectedItem()),
                    Integer.parseInt(minute.getSelectionModel().getSelectedItem())))
                    .isAfter(LocalDateTime.now());
        } catch (NumberFormatException | DateTimeException e) {
            return false;
        }
    }

    public void reloadTable(){
        getGatewayBundle().getEntityBundle().getTradeGateway().submitSession((gateway) -> {
            List<Trade> matchedTrades = gateway.findByUser(getAccountManager().getLoggedInUser());
            tradesList = FXCollections.observableArrayList(matchedTrades);
            trades.getItems().clear();
            trades.setItems(tradesList);
        });
    }*/
}

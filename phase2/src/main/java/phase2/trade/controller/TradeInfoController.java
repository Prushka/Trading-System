package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.sun.deploy.panel.TextFieldProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import phase2.trade.callback.ResultStatus;
import phase2.trade.callback.StatusCallback;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.item.Item;
import phase2.trade.presenter.SceneManager;
import phase2.trade.trade.Trade;
import phase2.trade.trade.command.CreateTrade;
import phase2.trade.trade.command.TradeCommand;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.swing.text.LabelView;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TradeInfoController extends AbstractController  implements Initializable {
    private TradeCommand tc;

    @FXML
    private VBox root;
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

    public TradeInfoController(SceneManager sceneManager){
        super(sceneManager);
        tc = new CreateTrade(getGatewayBundle(), getAccountManager().getLoggedInUser());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        meeting = new HBox();
        content = new VBox();

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
        type.setPadding(new Insets(5, 10, 5, 10));        tradeType = new Label("Type of Trade: ");
        isPermanent = new JFXComboBox<>();
        isPermanent.getItems().addAll("PERMANENT", "TEMPORARY");
        isPermanent.getSelectionModel().selectFirst();
        type.getChildren().addAll(tradeType, isPermanent);

        // Trading
        tradeButton = new JFXButton("Submit Trade");
        // tradeButton.setOnAction(e -> tradeButtonClicked());

        content.getChildren().addAll(users, items, dateTime, place, type, tradeButton);
        meeting.getChildren().addAll(content);
        root.getChildren().add(meeting);
    }

    public void tradeButtonClicked(){
        List<User> allUsers = new ArrayList<>();
        List<List<Item>> allItems = new ArrayList<>();
        if (isPermanent.getSelectionModel().getSelectedItem() == "PERMANENT"){
            tc.execute(new StatusCallback() { @Override
                                            public void call(Object result, ResultStatus resultStatus) {
                                                System.out.println("success");
                                            }
                                        }, year.getText(), month.getText(),
                    day.getText(), hour.getText(), minute.getText(), country.getText(), city.getText(), street.getText(),
                    streetNum.getText(), "true");
        }
        // trades.getItems().add(newTrade);
    }
}

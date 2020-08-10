package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import phase2.trade.controller.AbstractController;
import phase2.trade.gateway.GatewayBundle;
import phase2.trade.inventory.ItemList;
import phase2.trade.item.Item;

import javafx.scene.image.Image;
import phase2.trade.view.NoSelectionModel;

import java.net.URL;
import java.util.*;

public class MarketListController extends AbstractController implements Initializable {

    private final ItemList itemList;

    public ListView<Pane> listView;

    public VBox root;

    public JFXTextField searchName;

    public MarketListController(GatewayBundle gatewayBundle, ItemList itemList) {
        super(gatewayBundle);
        this.itemList = itemList;
    }

    private List<String> populate(int count) {
        List<String> temp = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            temp.add(String.valueOf(i));
        }
        return temp;
    }

    private Pane generateItemPreview(Item item) {
        HBox hBox = new HBox(40);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);
        hBox.setStyle("-fx-background-color: WHITE");
        hBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);
        ImageView imageView = new ImageView(new
                Image(getClass().getResourceAsStream("/test.png")));
        imageView.setFitWidth(120);
        imageView.setFitHeight(120);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Label categoryLabel = new Label(item.getCategory().name());


        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20");

        Label descriptionLabel = new Label(item.getDescription());

        JFXComboBox<String> comboBox = new JFXComboBox<>();
        comboBox.setStyle("-fx-text-fill: #293841;");
        comboBox.setItems(FXCollections.observableArrayList(populate(item.getQuantity())));

        comboBox.getSelectionModel().select(0);
        String price = "Lend";

        if (item.getPrice() != -1) {
            price = String.valueOf(item.getPrice());
        }

        Label priceLabel = new Label(price);
        Label uidLabel = new Label(String.valueOf(item.getUid()));
        Label ownerLabel = new Label(item.getOwner().getUserName());

        JFXButton addToCart = new JFXButton("Add To Cart");

        leftVBox.getChildren().addAll(categoryLabel, nameLabel, descriptionLabel);

        rightVBox.getChildren().addAll(priceLabel, uidLabel, ownerLabel);

        hBox.getChildren().addAll(imageView, leftVBox, region, comboBox, rightVBox, addToCart);
        return hBox;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setSelectionModel(new NoSelectionModel<>());
        for (Item item : itemList.getListOfItems()) {


            for (int i = 0; i < 5; i++) {
                listView.getItems().add(generateItemPreview(item));
            }
        }
    }


    private Set<Long> getItemIdsFrom(ObservableList<Item> observableList) {
        Set<Long> ids = new HashSet<>();
        for (Item item : observableList) {
            ids.add(item.getUid());
        }
        return ids;
    }

}

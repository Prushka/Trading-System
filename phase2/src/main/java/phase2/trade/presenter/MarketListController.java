package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import phase2.trade.callback.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.item.Item;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.NoSelectionModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MarketListController extends AbstractController implements Initializable {

    public ListView<Pane> listView;

    public JFXTextField searchName;

    public MarketListController(SceneManager sceneManager) {
        super(sceneManager);
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
        Command<List<Item>> getMarket = new GetMarketItems(getGatewayBundle(),getAccountManager().getLoggedInUser());

        getMarket.execute((result, resultStatus) -> {
            if (resultStatus == ResultStatus.NO_PERMISSION) {
                getPopupFactory().noPermission();
            } else {
                for (Item item : result) {
                    listView.getItems().add(generateItemPreview(item));
                }
            }
        });

    }

}

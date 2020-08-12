package phase2.trade.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import phase2.trade.item.Item;

import java.util.ArrayList;
import java.util.List;

public class MarketItemCell extends JFXListCell<Item> {

    public MarketItemCell() {
        super();
    }

    private Pane generateItemPreview(Item item) {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);

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
        hBox.getStyleClass().add("market-item-cell");
        return hBox;
    }


    private List<String> populate(int count) {
        List<String> temp = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            temp.add(String.valueOf(i));
        }
        return temp;
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(generateItemPreview(item));
            setText("");
        }
    }
}

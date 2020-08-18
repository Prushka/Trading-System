package phase2.trade.view;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListCell;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import phase2.trade.item.Item;
import phase2.trade.item.command.AddToCart;

import java.util.ArrayList;
import java.util.List;

public class MarketItemCell extends JFXListCell<Item> {

    private final AddToCart addToCartCommand;

    private final NotificationFactory notificationFactory;

    public MarketItemCell(AddToCart addToCartCommand, NotificationFactory notificationFactory) {
        this.addToCartCommand = addToCartCommand;
        this.notificationFactory = notificationFactory;
    }

    private Pane generateItemPreview(Item item) {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);

        hBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Label categoryLabel = new Label(item.getCategory().name());


        Label nameLabel = new Label(item.getName());
        nameLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 20");

        Label descriptionLabel = new Label(item.getDescription());
        descriptionLabel.setMaxWidth(350);

        JFXComboBox<String> comboBox = new JFXComboBox<>();
        comboBox.setStyle("-fx-text-fill: #293841;");
        comboBox.setItems(FXCollections.observableArrayList(populate(item.getQuantity())));

        comboBox.getSelectionModel().select(0);
        String price = "Lend";

        if (item.getPrice() != -1) {
            price = String.valueOf(String.format("%.2f", item.getPrice()));
        }

        Label sellOrLendLabel = new Label("");
        switch (item.getWillingness()) {
            case LEND:
                sellOrLendLabel.setText("For LEND");
                sellOrLendLabel.setStyle("-fx-text-fill: rgb(223,121,145);-fx-font-weight: BOLD");
                break;
            case SELL:
                sellOrLendLabel.setText("Sell: " + price + "$"); // TODO: currency system?
                sellOrLendLabel.setStyle("-fx-text-fill: rgb(227,98,9);-fx-font-weight: BOLD");
                break;
        }

        Label uidLabel = new Label("UID: " + item.getUid());
        Label ownerLabel = new Label("Owner: " + item.getOwner().getName());
        ownerLabel.setStyle("-fx-text-fill: rgb(92,141,152);-fx-font-weight: BOLD");


        JFXButton addToCart = new JFXButton("Add To Cart");
        addToCart.setOnAction(event -> {
            addToCartCommand.setItems(item);
            addToCartCommand.execute((result, status) -> {
                status.setSucceeded(() -> getPopupFactory().toast(3, "Successfully added to cart"));
                status.setExist(() -> getPopupFactory().toast(3, "The item is already in your cart"));
                status.handle(getPopupFactory());
            });
        });

        leftVBox.getChildren().addAll(categoryLabel, nameLabel, descriptionLabel, ownerLabel);

        rightVBox.getChildren().addAll(uidLabel, sellOrLendLabel);

        Node svg = new ImageFactory().generateGraphic(item.getCategory().resourcePath, -1, false, 120, 120);
        if (svg != null) {
            HBox.setMargin(svg,new Insets(0,20,0,0));
            hBox.getChildren().addAll(svg);
        }
        hBox.getChildren().addAll(leftVBox, region, comboBox, rightVBox, addToCart);
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

    private NotificationFactory getPopupFactory() {
        return notificationFactory;
    }
}

package phase2.trade.controller.market;

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
import phase2.trade.view.ImageFactory;
import phase2.trade.view.NotificationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * The market item cell used to generate the layout for every Item.
 *
 * @author Dan Lyu
 */
public class MarketItemCell extends JFXListCell<Item> {

    private final AddToCart addToCartCommand;

    private final NotificationFactory notificationFactory;

    /**
     * Constructs a new Market item cell.
     *
     * @param addToCartCommand    the add to cart command
     * @param notificationFactory the notification factory
     */
    public MarketItemCell(AddToCart addToCartCommand, NotificationFactory notificationFactory) {
        this.addToCartCommand = addToCartCommand;
        this.notificationFactory = notificationFactory;
    }

    private Pane generateItemPreview(Item item) {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);

        hBox.getStyleClass().add("market-item-cell");

        hBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        Label categoryLabel = new Label(item.getCategory().name());


        Label nameLabel = new Label(item.getName());
        nameLabel.getStyleClass().add("name");

        Label descriptionLabel = new Label(item.getDescription());
        descriptionLabel.setMaxWidth(350);

        JFXComboBox<String> quantityComboBox = new JFXComboBox<>();
        quantityComboBox.getStyleClass().add("combo");
        quantityComboBox.setItems(FXCollections.observableArrayList(populate(item.getQuantity())));

        quantityComboBox.getSelectionModel().select(0);
        String price = "Lend";

        if (item.getPrice() != -1) {
            price = String.valueOf(String.format("%.2f", item.getPrice()));
        }

        Label sellOrLendLabel = new Label("");
        switch (item.getWillingness()) {
            case LEND:
                sellOrLendLabel.setText("For LEND");
                sellOrLendLabel.getStyleClass().add("lend");
                break;
            case SELL:
                sellOrLendLabel.setText("Sell: " + price + "$"); // TODO: currency system
                sellOrLendLabel.getStyleClass().add("sell");
                break;
        }

        Label uidLabel = new Label("UID: " + item.getUid());
        Label ownerLabel = new Label("Owner: " + item.getOwner().getName());
        ownerLabel.setStyle("-fx-text-fill: rgb(92,141,152);-fx-font-weight: BOLD");


        JFXButton addToCart = new JFXButton("Add To Cart");
        addToCart.setOnAction(event -> {
            addToCartCommand.setToUpdate(item);
            addToCartCommand.execute((result, status) -> {
                status.setSucceeded(() -> getNotificationFactory().toast(3, "Successfully added to cart"));
                status.handle(getNotificationFactory());
            }, quantityComboBox.getValue());
        });

        leftVBox.getChildren().addAll(categoryLabel, nameLabel, descriptionLabel, ownerLabel);

        rightVBox.getChildren().addAll(uidLabel, sellOrLendLabel);

        Node svg = new ImageFactory().generateGraphic(item.getCategory().resourcePath, -1, false, 120, 120);
        if (svg != null) {
            HBox.setMargin(svg, new Insets(0, 20, 0, 0));
            hBox.getChildren().addAll(svg);
        }
        hBox.getChildren().addAll(leftVBox, region, quantityComboBox, rightVBox, addToCart);
        return hBox;
    }


    private List<String> populate(int count) {
        count = Math.min(count, 10);
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

    private NotificationFactory getNotificationFactory() {
        return notificationFactory;
    }
}

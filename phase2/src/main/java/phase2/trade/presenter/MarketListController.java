package phase2.trade.presenter;

import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import phase2.trade.Main;
import phase2.trade.callback.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.NoSelectionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class MarketListController extends AbstractController implements Initializable {

    public JFXListView<Pane> listView;

    public JFXTextField searchName;

    private final HBox top;

    public MarketListController(ControllerResources controllerResources, HBox top) {
        super(controllerResources);
        this.top = top;
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setSelectionModel(new NoSelectionModel<>());
        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);

        top.setSpacing(20);
        JFXButton search = new JFXButton();
        JFXCheckBox lend = new JFXCheckBox("Lend");
        JFXCheckBox sell = new JFXCheckBox("Sell");

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().addAll(lend, sell);

        JFXComboBox<String> category = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))));
        JFXToggleButton includeMine = new JFXToggleButton();
        includeMine.setText("includeMine");

        JFXTextField priceMinInclusive = new JFXTextField();
        priceMinInclusive.setPromptText("Min Price (Inclusive)");
        priceMinInclusive.setLabelFloat(true);
        JFXTextField priceMaxInclusive = new JFXTextField();
        priceMaxInclusive.setPromptText("Max Price (Inclusive)");
        priceMaxInclusive.setLabelFloat(true);

        Label label = new Label("-");


        // custom svg elsewhere
        SVGGlyph arrow = new SVGGlyph(0,
                "FULLSCREEN",
                "M.22,18.28a.75.75,0,0,1,0-1.06l5.4-5.4a7.258,7.258,0,1,1,1.061,1.061l-5.4,5.4a.75.75,0,0,1-1.061,0ZM5.5,7.25A5.75,5.75,0,1,0,11.25,1.5,5.757,5.757,0,0,0,5.5,7.25Z",
                Color.WHITE);

        arrow.setSize(35, 35);
        search.setGraphic(arrow);
        search.setRipplerFill(Color.WHITE);

        top.getChildren().clear();
        top.getChildren().addAll(vBox, category, includeMine, priceMinInclusive, label, priceMaxInclusive, search);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                for (Item item : result) {
                    listView.getItems().add(generateItemPreview(item));
                }
            });
            resultStatus.handle(getPopupFactory());
        });

    }
}

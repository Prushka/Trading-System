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
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.MarketItemCellFactory;
import phase2.trade.view.NoSelectionModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "market_list.fxml")
public class MarketListController extends AbstractController implements Initializable {

    public JFXListView<Item> listView;

    public JFXTextField searchName;

    public MarketListController(ControllerResources controllerResources) {
        super(controllerResources);
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listView.setSelectionModel(new NoSelectionModel<>());
        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);

        listView.setCellFactory(new MarketItemCellFactory());
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

        getPane("topBar").getChildren().clear();
        getPane("topBar").getChildren().addAll(vBox, category, includeMine, priceMinInclusive, label, priceMaxInclusive, search);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                for (Item item : result) {
                    listView.getItems().add(item);
                }
            });
            resultStatus.handle(getPopupFactory());
        });

    }
}

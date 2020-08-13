package phase2.trade.controller.market;

import com.jfoenix.controls.*;
import com.jfoenix.svg.SVGGlyph;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddToCart;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.ListViewGenerator;
import phase2.trade.view.MarketItemCell;
import phase2.trade.view.NoSelectionModel;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "market_list.fxml")
public class MarketListController extends AbstractController implements Initializable {

    public JFXListView<Item> listView;

    public JFXTextField searchName;

    private ListViewGenerator<Item> listViewGenerator;

    public MarketListController(ControllerResources controllerResources) {
        super(controllerResources);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        listViewGenerator = new ListViewGenerator<>(listView);

        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetMarketItems::new);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                for (Item item : result) {
                    listViewGenerator.addElement(item);
                }
                afterFetch();
            });
            resultStatus.handle(getPopupFactory());
        });


    }

    private void afterFetch() {
        listView.setSelectionModel(new NoSelectionModel<>());
        AddToCart addToCartCommand = getCommandFactory().getCommand(AddToCart::new);
        listView.setCellFactory(param -> new MarketItemCell(addToCartCommand));
        JFXButton search = new JFXButton();
        JFXCheckBox lend = new JFXCheckBox("Lend");
        JFXCheckBox sell = new JFXCheckBox("Sell");

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().addAll(lend, sell);

        JFXComboBox<String> category = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))));
        category.setPromptText("Category");
        category.setLabelFloat(true);
        category.getItems().add("ALL");

        JFXToggleButton includeMine = new JFXToggleButton();
        includeMine.setText("includeMine");

        JFXTextField priceMinInclusive = new JFXTextField();
        priceMinInclusive.setPromptText("Min Price");
        priceMinInclusive.setLabelFloat(true);
        JFXTextField priceMaxInclusive = new JFXTextField();
        priceMaxInclusive.setPromptText("Max Price");
        priceMaxInclusive.setLabelFloat(true);

        Pattern doublePattern = Pattern.compile("\\d+\\.?\\d?");
        listViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.LEND))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.SELL))
                .addComboBox(category, (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch))
                .addToggleButton(includeMine, ((entity, toMatch) -> entity.getOwner().getUid().
                        equals(getAccountManager().getLoggedInUser().getUid())))
                .addSearch(priceMinInclusive, ((entity, toMatch) -> {
                    if(!doublePattern.matcher(toMatch).matches()){
                        getPopupFactory().toast(3, "Please enter a double in Price Min","CLOSE");
                        return true;
                    }else{
                        return entity.getPrice() >= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(priceMaxInclusive, ((entity, toMatch) -> {
                    if(!doublePattern.matcher(toMatch).matches()){
                        getPopupFactory().toast(3, "Please enter a double in Price Max","CLOSE");
                        return true;
                    }else{
                        return entity.getPrice() <= Double.parseDouble(toMatch);
                    }
                }));

        lend.setSelected(true);
        sell.setSelected(true);
        includeMine.setSelected(true);
        priceMinInclusive.setMaxWidth(80);
        priceMaxInclusive.setMaxWidth(80);
        Label label = new Label("-");


        // custom svg elsewhere
        SVGGlyph arrow = new SVGGlyph(0,
                "FULLSCREEN",
                "M.22,18.28a.75.75,0,0,1,0-1.06l5.4-5.4a7.258,7.258,0,1,1,1.061,1.061l-5.4,5.4a.75.75,0,0,1-1.061,0ZM5.5,7.25A5.75,5.75,0,1,0,11.25,1.5,5.757,5.757,0,0,0,5.5,7.25Z",
                Color.WHITE);

        arrow.setSize(35, 35);
        search.setGraphic(arrow);
        search.setRipplerFill(Color.WHITE);

        getPane("topBar").getChildren().setAll(vBox, category, includeMine, priceMinInclusive, label, priceMaxInclusive, search);
        listViewGenerator.build();
    }
}

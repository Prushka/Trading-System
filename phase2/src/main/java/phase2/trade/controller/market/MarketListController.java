package phase2.trade.controller.market;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.controller.ListController;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.AddToCart;
import phase2.trade.item.command.GetMarketItems;
import phase2.trade.view.NoSelectionModel;
import phase2.trade.view.NodeFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * The market list controller.
 *
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_list_view.fxml")
public class MarketListController extends ListController<Item> implements Initializable {

    private ComboBox<String> countryCombo;
    private ComboBox<String> provinceCombo;
    private ComboBox<String> cityCombo;

    private final Command<List<Item>> getMarket;

    /**
     * Constructs a new market list controller.
     *
     * @param controllerResources the controller resources
     */
    public MarketListController(ControllerResources controllerResources) {
        super(controllerResources, false);
        getMarket = getCommandFactory().getCommand(GetMarketItems::new);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(result);
                afterFetch();
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    @Override
    public void reload() {
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                reloadNewDisplayData(result);
            });
            resultStatus.handle(getNotificationFactory());
        });
        super.reload();
    }

    private void afterFetch() {
        listView.setSelectionModel(new NoSelectionModel<>());
        AddToCart addToCartCommand = getCommandFactory().getCommand(AddToCart::new);
        listView.setCellFactory(param -> new MarketItemCell(addToCartCommand, getNotificationFactory()));
        JFXCheckBox lend = new JFXCheckBox("Lend");
        JFXCheckBox sell = new JFXCheckBox("Sell");

        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER_LEFT);
        vBox.getChildren().addAll(lend, sell);

        JFXComboBox<String> categoryCombo = getNodeFactory().getComboBoxByType(NodeFactory.ComboBoxType.Category, "ALL");

        JFXToggleButton includeMine = new JFXToggleButton();
        includeMine.setText("includeMine");

        TextField priceMinInclusive = getNodeFactory().getDefaultTextField("Min Price");
        TextField priceMaxInclusive = getNodeFactory().getDefaultTextField("Max Price");
        TextField name = getNodeFactory().getDefaultTextField("Search Name");
        TextField description = getNodeFactory().getDefaultTextField("Search Description");

        getNodeFactory().getAddressComboBoxes((a, b, c) -> {
            countryCombo = a;
            provinceCombo = b;
            cityCombo = c;
        }, getConfigBundle().getGeoConfig());

        Pattern doublePattern = Pattern.compile("\\d+\\.?\\d?");
        listViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.LEND))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.SELL))
                .addComboBox(categoryCombo, (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch))
                .addToggleButton(includeMine, ((entity, toMatch) -> entity.getOwner().getUid().
                        equals(getAccountManager().getLoggedInUser().getUid())))
                .addSearch(priceMinInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getNotificationFactory().toast(3, "Please enter a double in Price Min", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() >= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(priceMaxInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getNotificationFactory().toast(3, "Please enter a double in Price Max", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() <= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(priceMaxInclusive, ((entity, toMatch) -> {
                    if (!doublePattern.matcher(toMatch).matches()) {
                        getNotificationFactory().toast(3, "Please enter a double in Price Max", "CLOSE");
                        return true;
                    } else {
                        return entity.getPrice() <= Double.parseDouble(toMatch);
                    }
                }))
                .addSearch(name, ((entity, toMatch) -> {
                    String lowerCaseFilter = toMatch.toLowerCase();
                    return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
                }))
                .addSearch(description, ((entity, toMatch) -> {
                    String lowerCaseFilter = toMatch.toLowerCase();
                    return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
                }))
                .addComboBox(countryCombo, (entity, toMatch) -> entity.getOwner().getAddressBook().getSelectedAddress().getCountry().equalsIgnoreCase(toMatch))
                .addComboBox(provinceCombo, (entity, toMatch) -> entity.getOwner().getAddressBook().getSelectedAddress().getTerritory().equalsIgnoreCase(toMatch))
                .addComboBox(cityCombo, (entity, toMatch) -> entity.getOwner().getAddressBook().getSelectedAddress().getCity().equalsIgnoreCase(toMatch))
        ;

        lend.setSelected(true);
        sell.setSelected(true);
        includeMine.setSelected(true);
        priceMinInclusive.setMaxWidth(80);
        priceMaxInclusive.setMaxWidth(80);
        Label label = new Label("|");

        getPane(DashboardPane.TOP).getChildren().setAll(vBox, name, description, categoryCombo, includeMine);

        listViewGenerator.build();

        countryCombo.setPrefWidth(200);
        cityCombo.setPrefWidth(200);
        provinceCombo.setPrefWidth(200);

        Pane left = getPane(DashboardPane.LEFT);

        left.getStyleClass().addAll("dashboard-top-tool-bar", "dashboard-left-tool-bar");
        Label quantityTip = new Label("Max Quantity 10");
        Button clear = getNodeFactory().getDefaultFlatButton("Clear", "");
        clear.setOnAction(e -> {
            name.setText("");
            description.setText("");
            priceMaxInclusive.setText("");
            priceMinInclusive.setText("");
            categoryCombo.getSelectionModel().clearSelection();
            countryCombo.getSelectionModel().clearSelection();
            cityCombo.getSelectionModel().clearSelection();
            provinceCombo.getSelectionModel().clearSelection();
        });
        VBox.setMargin(clear, new Insets(20, 0, 0, 0));
        VBox.setMargin(priceMinInclusive, new Insets(20, 0, 0, 0));
        VBox.setMargin(countryCombo, new Insets(0, 20, 0, 20));
        left.getChildren().setAll(priceMinInclusive, label, priceMaxInclusive,
                countryCombo, provinceCombo, cityCombo, quantityTip, clear);

    }
}

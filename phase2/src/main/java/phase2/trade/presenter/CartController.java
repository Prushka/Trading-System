package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AddItemController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class CartController extends GeneralTableViewController<Item> implements Initializable {

    private final ItemListType itemListType;

    public CartController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, false);
        this.itemListType = itemListType;
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location,resources);
        tableViewGenerator.addColumn("Name", "name").addColumn("Description", "description").addColumn("Category", "category")
                .addColumn("Ownership", "ownership").addColumn("Quantity", "quantity").addColumn("Price", "price")
                .addColumn("Willingness", "willingness").addColumn("Owner", param -> {
                    if (param.getValue() != null) {
                        return new SimpleStringProperty(param.getValue().getOwner().getUserName());
                    } else {
                        return new SimpleStringProperty("null");
                    }
                });

        JFXButton deleteButton = new JFXButton("Delete");
        JFXButton trade = new JFXButton("Trade");

        buttons.getChildren().addAll(deleteButton, trade);

        buttonsToDisable = FXCollections.observableArrayList(deleteButton, trade);

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            displayData.removeAll(itemsSelected);
        });

        JFXTextField searchName = new JFXTextField();
        searchName.setPromptText("Search Name");
        searchName.setLabelFloat(true);

        JFXTextField searchDescription = new JFXTextField();
        searchDescription.setPromptText("Search Description");
        searchDescription.setLabelFloat(true);

        JFXComboBox<String> category = new JFXComboBox<>(FXCollections.observableArrayList(Arrays.asList(Stream.of(Category.values()).map(Category::name).toArray(String[]::new))));
        category.setPromptText("Category");
        category.getItems().add("ALL");
        category.setLabelFloat(true);


        JFXCheckBox lend = new JFXCheckBox("Wish To Lend");
        JFXCheckBox sell = new JFXCheckBox("Wish To Sell");
        JFXCheckBox privateCheckBox = new JFXCheckBox("Private");

        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.LEND))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.SELL))
                .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.NOPE))
                .addComboBox(category, (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch))
                .addSearch(searchName, (entity, toMatch) -> {
                    String lowerCaseFilter = toMatch.toLowerCase();
                    return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
                })
                .addSearch(searchDescription, (entity, textField) -> {
                    String lowerCaseFilter = textField.toLowerCase();
                    return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
                });

        getPane("topBar").getChildren().setAll(searchName, searchDescription, category, lend, sell, privateCheckBox);
        lend.setSelected(true);
        sell.setSelected(true);
        privateCheckBox.setSelected(true);

        tableViewGenerator.build();
    }
}

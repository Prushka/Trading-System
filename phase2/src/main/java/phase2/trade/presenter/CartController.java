package phase2.trade.presenter;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.AddItemController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.ItemEditor;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.item.command.UpdateInventoryItems;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "item_list.fxml")
public class CartController extends AbstractController implements Initializable {

    private final ItemListType itemListType;

    public TableView<Item> tableView;

    public HBox buttons;

    private List<Button> buttonsToDisable;

    private ObservableList<Item> displayData;

    public CartController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        displayData = FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems());


        tableView.setEditable(true);


        TableViewGenerator<Item> tableViewGenerator = new TableViewGenerator<>(displayData, 100, tableView);
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

    private Set<Long> getItemIdsFrom(List<? extends Item> list) {
        Set<Long> ids = new HashSet<>();
        for (Item item : list) {
            ids.add(item.getUid());
        }
        return ids;
    }

    private ObservableList<Item> getSelected() {
        ObservableList<Item> itemsSelected;
        itemsSelected = tableView.getSelectionModel().getSelectedItems();
        return itemsSelected;
    }

    public void addWindow(ObservableList<Item> displayData) {
        AddItemController addItemController = new AddItemController(getControllerResources(), itemListType, displayData);
        getSceneManager().loadPane(addItemController);
    }

    private void disableButtons(boolean value) {
        for (Button button : buttonsToDisable) {
            button.setDisable(value);
        }
    }
}

package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.market.MarketListController;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.Item;
import phase2.trade.item.command.RemoveItem;
import phase2.trade.user.User;
import phase2.trade.view.window.GeneralVBoxAlert;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class CartController extends ItemController implements Initializable {

    private final ItemListType itemListType;

    public CartController(ControllerResources controllerResources, ItemListType itemListType) {
        super(controllerResources, true, false);
        this.itemListType = itemListType;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        setDisplayData(FXCollections.observableArrayList(getAccountManager().getLoggedInUser().getItemList(itemListType).getSetOfItems()));

        addNameColumn(false);
        addDescriptionColumn(false);
        addOwnershipColumn(false);
        addQuantityColumn(false);
        addPriceColumn(false);
        addCategoryColumn(false);
        addWillingnessColumn(false);
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes();

        JFXComboBox<User> newUser = new JFXComboBox<>();
        getGatewayBundle().getEntityBundle().getUserGateway().submitSession((gateway) -> {
            List<User> matchedUsers = gateway.findAll();
            newUser.getItems().setAll(matchedUsers);
        });

        JFXButton addButton = new JFXButton("Add Items to Borrow/ Buy");
        addButton.setOnAction(e -> itemsClicked());
        JFXButton deleteButton = new JFXButton("Delete");

        buttonPane.getChildren().addAll(newUser);
        addButton(addButton,deleteButton);

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemListType(itemListType);
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        deleteButton.setOnAction(event -> {
            ObservableList<Item> itemsSelected = getSelected();
            displayData.removeAll(itemsSelected);
        });

        tableViewGenerator.build();
    }

    public void itemsClicked(){
        GeneralVBoxAlert popup = getPopupFactory().vBoxAlert("Available Items", "");
        popup.addNodes(getSceneManager().loadPane(new MarketListController(getControllerResources())));
        popup.display();
    }
}

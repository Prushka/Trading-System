package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import phase2.trade.callback.ResultStatusCallback;
import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.status.ResultStatus;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.GeneralTableViewController;
import phase2.trade.database.TriConsumer;
import phase2.trade.inventory.ItemListType;
import phase2.trade.item.*;
import phase2.trade.item.command.*;
import phase2.trade.view.NodeFactory;
import phase2.trade.view.window.GeneralSplitAlert;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class ItemManageController extends ItemController implements Initializable {

    public ItemManageController(ControllerResources controllerResources) {
        super(controllerResources, true, false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<Item>> getMarket = getCommandFactory().getCommand(GetAllItems::new);
        getMarket.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(FXCollections.observableArrayList(result));
                afterFetch();
            });
            resultStatus.handle(getPopupFactory());
        });
    }

    private void afterFetch() {
        tableViewGenerator
                .addColumnEditable("Name", "name", event ->
                        shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                        }, ItemEditor::alterName))

                .addColumnEditable("Description", "description", getConfigBundle().getUiConfig().getItemDescriptionPrefWidth(),
                        event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                        }, ItemEditor::alterDescription))

                .addColumn("Ownership", "ownership")

                .addColumnEditable("Quantity", param -> new SimpleStringProperty(String.valueOf(param.getValue().getQuantity())),
                        event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                        }, ItemEditor::alterQuantity))

                .addColumnEditable("Price", param -> new SimpleStringProperty(String.valueOf(param.getValue().getPrice())),
                        event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                        }, ItemEditor::alterPrice))

                .addColumnEditable("Category", "category", event -> {
                    new ItemEditor(event.getRowValue()).alterCategory(event.getNewValue(), resultStatus -> {
                    });
                    updateItem(event.getRowValue());
                }, ComboBoxTableCell.forTableColumn(getNodeFactory().getEnumAsObservableString(Category.class)))

                .addColumnEditable("Willingness", "willingness", event -> {
                    ItemEditor itemEditor = new ItemEditor(event.getRowValue());
                    itemEditor.alterWillingness(event.getNewValue(), resultStatus -> {
                    });
                    updateItem(event.getRowValue());
                }, ComboBoxTableCell.forTableColumn(getNodeFactory().getEnumAsObservableString(Willingness.class)))

                .addColumn("UID", "uid");

        JFXButton deleteButton = new JFXButton("Delete");

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> {
            command.setItemIds(idsRemoved);
        }), Item::getUid);

        addSearchField("Search Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });

        addSearchField("Search Description", (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
        });

        addComboBox(
                getNodeFactory().getEnumAsObservableString(Category.class),
                "Category", "ALL",
                (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));

        addComboBox(
                getNodeFactory().getEnumAsObservableString(Ownership.class),
                "Ownership", "ALL",
                (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));


        CheckBox lend = new JFXCheckBox("Wish To Lend");
        CheckBox sell = new JFXCheckBox("Wish To Sell");
        CheckBox privateCheckBox = new JFXCheckBox("Private");

        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.Lend))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.Sell))
                .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.Private));

        getPane("topBar").getChildren().addAll(lend, sell, privateCheckBox);
        lend.setSelected(true);
        sell.setSelected(true);
        privateCheckBox.setSelected(true);

        Button reviewItems = new JFXButton("Mark selected items as reviewItems");
        reviewItems.setOnAction(event -> {
            if (getSelected().size() == 0) {
                nothingSelectedToast();
                return;
            }
            Command<Item> alterItemOwnership = getCommandFactory().getCommand(AlterItemOwnership::new,
                    c -> c.setItemId(getSelectedItemsIds().toArray(new Long[0])));
            alterItemOwnership.execute((result, status) -> {
                status.setSucceeded(() -> tableView.refresh());
                status.handle(getPopupFactory());
            }, Ownership.OWNER.name());
        });
        addButton(reviewItems);
        tableViewGenerator.build();
    }

}

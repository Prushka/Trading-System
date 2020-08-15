package phase2.trade.item.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.cell.ComboBoxTableCell;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.GeneralTableViewController;
import phase2.trade.database.TriConsumer;
import phase2.trade.item.*;
import phase2.trade.item.command.UpdateItems;

import java.util.ArrayList;
import java.util.List;

public class ItemTableController extends GeneralTableViewController<Item> implements Initializable {

    public ItemTableController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable);
    }

    protected void shortenAlter(Item item, String newValue, StatusCallback statusCallback, TriConsumer<ItemEditor, String, StatusCallback> consumer) {
        consumer.consume(new ItemEditor(item), newValue, statusCallback);
        updateItem(item);
    }

    protected void shortenAlterOfSelected(String newValue, StatusCallback statusCallback, TriConsumer<ItemEditor, String, StatusCallback> consumer) {
        consumer.consume(new ItemEditor(getSelected()), newValue, statusCallback);
        updateItem(getSelected());
    }

    protected void updateItem(Item item) {
        List<Item> items = new ArrayList<>();
        items.add(item);
        updateItem(items);
    }

    protected void updateItem(List<Item> items) {
        disableButtons(true);
        Command<?> command = getCommandFactory().getCommand(UpdateItems::new, c -> {
            c.setItemsToUpdate(items);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setAfter(() -> {
                disableButtons(false);
                tableView.refresh();
            });
            resultStatus.handle(getPopupFactory());
        });
    }

    @Override
    protected ObservableList<Item> getSelected(){
        if(super.getSelected().size() == 0){
            nothingSelectedToast();
        }
        return super.getSelected();
    }

    protected List<Long> getSelectedItemsIds() {
        List<Long> ids = new ArrayList<>();
        getSelected().forEach(item -> ids.add(item.getUid()));
        return ids;
    }

    protected void nothingSelectedToast() {
        getPopupFactory().toast(3, "You didn't select anything", "CLOSE");
    }

    // this is already a super class of all Item Table views, they will reside here
    protected void addNameColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Name", "name", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterName));
        } else {
            tableViewGenerator.addColumn("Name", "name");
        }
    }

    // to decouple this would be unnecessary since some columns require cellfactory or cellfactory + cellvaluefactory while some don't
    protected void addDescriptionColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Description", "description", getConfigBundle().getUiConfig().getItemDescriptionPrefWidth(),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterDescription));
        } else {
            tableViewGenerator.addColumn("Description", "description");
        }
    }

    protected void addOwnershipColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Ownership", "ownership",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterOwnership),
                    ComboBoxTableCell.forTableColumn(getNodeFactory().getEnumAsObservableString(Ownership.class)));
        } else {
            tableViewGenerator.addColumn("Ownership", "ownership");
        }
    }

    protected void addCategoryColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Category", "category",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterCategory),
                    ComboBoxTableCell.forTableColumn(getNodeFactory().getEnumAsObservableString(Category.class)));
        } else {
            tableViewGenerator.addColumn("Category", "category");
        }
    }

    protected void addWillingnessColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Willingness", "willingness",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterWillingness),
                    ComboBoxTableCell.forTableColumn(getNodeFactory().getEnumAsObservableString(Willingness.class)));
        } else {
            tableViewGenerator.addColumn("Willingness", "willingness");
        }
    }

    protected void addQuantityColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Quantity", param -> new SimpleStringProperty(String.valueOf(param.getValue().getQuantity())),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterQuantity));
        } else {
            tableViewGenerator.addColumn("Quantity", "quantity");
        }
    }

    protected void addPriceColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Price", param -> new SimpleStringProperty(String.valueOf(param.getValue().getPrice())),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterPrice));
        } else {
            tableViewGenerator.addColumn("Price", "price");
        }
    }

    protected void addUIDColumn() {
        tableViewGenerator.addColumn("UID", "uid");
    }

    protected void addSearchName() {
        addSearchField("Search Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    protected void addSearchDescription() {
        addSearchField("Search Description", (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    protected void addCategoryComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(Category.class),
                "Category", "ALL",
                (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));
    }

    protected void addOwnershipComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(Ownership.class),
                "Ownership", "ALL",
                (entity, toMatch) -> entity.getOwnership().name().equalsIgnoreCase(toMatch));
    }

    protected void addWillingnessCheckBoxes() {
        CheckBox lend = new JFXCheckBox("Wish To Lend");
        CheckBox sell = new JFXCheckBox("Wish To Sell");
        CheckBox privateCheckBox = new JFXCheckBox("Private");

        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.Lend))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.Sell))
                .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.Private));

        lend.setSelected(true);
        sell.setSelected(true);
        privateCheckBox.setSelected(true);
        getPane("topBar").getChildren().addAll(lend, sell, privateCheckBox);
    }
}

package phase2.trade.item.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.util.Callback;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractEditableTableController;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.UpdateItems;

import java.util.List;

public class ItemController extends AbstractEditableTableController<Item, ItemEditor> implements Initializable {

    public ItemController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources, ifMultipleSelection, ifEditable, ItemEditor::new);
    }

    @Override
    protected void updateEntity(List<Item> entities) {
        disableButtons(true);
        Command<?> command = getCommandFactory().getCommand(UpdateItems::new, c -> {
            c.setItemsToUpdate(entities);
        });
        command.execute((result, resultStatus) -> {
            resultStatus.setAfter(() -> {
                disableButtons(false);
                tableView.refresh();
            });
            resultStatus.handle(getPopupFactory());
        });
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
                    getNodeFactory().getEnumAsObservableString(Ownership.class));
        } else {
            tableViewGenerator.addColumn("Ownership", "ownership");
        }
    }

    protected void addCategoryColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Category", "category",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterCategory),
                    getNodeFactory().getEnumAsObservableString(Category.class));
        } else {
            tableViewGenerator.addColumn("Category", "category");
        }
    }

    protected void addWillingnessColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Willingness", "willingness",
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterWillingness),
                    getNodeFactory().getEnumAsObservableString(Willingness.class));
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

    protected void addOwnerColumn() {
        tableViewGenerator.addColumn("Name", param -> new SimpleStringProperty(param.getValue().getOwner().getName()));
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

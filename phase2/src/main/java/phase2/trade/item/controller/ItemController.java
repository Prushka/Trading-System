package phase2.trade.item.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerResources;
import phase2.trade.controller.DashboardPane;
import phase2.trade.controller.EditableTableController;
import phase2.trade.controller.market.MarketListController;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.Category;
import phase2.trade.item.Item;
import phase2.trade.item.Ownership;
import phase2.trade.item.Willingness;
import phase2.trade.item.command.UpdateItems;

import java.util.List;

/**
 * The base Item controller.<p>
 * It is responsible for adding columns and adding a combination of view nodes (TextFields, ComboBoxes, CheckBoxes, Toggle) fot the {@link phase2.trade.view.FilterGroup}
 *
 * @author Dan Lyu
 * @see AbstractCartController
 * @see InventoryController
 * @see ItemManageController
 * @see CartController
 */
public abstract class ItemController extends EditableTableController<Item, ItemEditor> implements Initializable {

    /**
     * Constructs a new Item controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     * @param ifEditable          the if editable
     */
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
                publishGateway(MarketListController.class, ItemManageController.class, InventoryController.class);
            });
            resultStatus.handle(getNotificationFactory());
        });
    }

    /**
     * Add name column.
     *
     * @param editable the editable
     */
    protected void addNameColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Name", "name", event ->
                    shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterName));
        } else {
            tableViewGenerator.addColumn("Name", "name");
        }
    }

    /**
     * Add description column.
     *
     * @param editable the editable
     */
    protected void addDescriptionColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Description", "description", getConfigBundle().getUiConfig().getItemDescriptionPrefWidth(),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterDescription));
        } else {
            tableViewGenerator.addColumn("Description", "description", getConfigBundle().getUiConfig().getItemDescriptionPrefWidth());
        }
    }

    /**
     * Add ownership column.
     *
     * @param editable the editable
     */
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

    /**
     * Add category column.
     *
     * @param editable the editable
     */
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

    /**
     * Add willingness column.
     *
     * @param editable the editable
     */
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

    /**
     * Add quantity column.
     *
     * @param editable the editable
     */
    protected void addQuantityColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Quantity", param -> new SimpleStringProperty(String.valueOf(param.getValue().getQuantity())),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterQuantity));
        } else {
            tableViewGenerator.addColumn("Quantity", "quantity");
        }
    }

    /**
     * Add price column.
     *
     * @param editable the editable
     */
    protected void addPriceColumn(boolean editable) {
        if (editable) {
            tableViewGenerator.addColumnEditable("Price", param -> new SimpleStringProperty(String.valueOf(param.getValue().getPrice())),
                    event -> shortenAlter(event.getRowValue(), event.getNewValue(), resultStatus -> {
                    }, ItemEditor::alterPrice));
        } else {
            tableViewGenerator.addColumn("Price", "price");
        }
    }

    /**
     * Add owner column.
     */
    protected void addOwnerColumn() {
        tableViewGenerator.addColumn("Owner", param -> new SimpleStringProperty(param.getValue().getOwner().getName()));
    }

    /**
     * Add uid column.
     */
    protected void addUIDColumn() {
        tableViewGenerator.addColumn("UID", "uid");
    }

    /**
     * Add search name.
     */
    protected void addSearchName() {
        addSearchField("Search Name", (entity, toMatch) -> {
            String lowerCaseFilter = toMatch.toLowerCase();
            return String.valueOf(entity.getName()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    /**
     * Add search description.
     */
    protected void addSearchDescription() {
        addSearchField("Search Description", (entity, textField) -> {
            String lowerCaseFilter = textField.toLowerCase();
            return String.valueOf(entity.getDescription()).toLowerCase().contains(lowerCaseFilter);
        });
    }

    /**
     * Add category combo box.
     */
    protected void addCategoryComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(Category.class),
                "Category", "ALL",
                (entity, toMatch) -> entity.getCategory().name().equalsIgnoreCase(toMatch));
    }

    /**
     * Add ownership combo box.
     */
    protected void addOwnershipComboBox() {
        addComboBox(
                getNodeFactory().getEnumAsObservableString(Ownership.class),
                "Ownership", "ALL",
                (entity, toMatch) -> entity.getOwnership().name().equalsIgnoreCase(toMatch));
    }

    /**
     * Add willingness check boxes.
     *
     * @param includePrivate the include private
     */
    protected void addWillingnessCheckBoxes(boolean includePrivate) {
        CheckBox lend = new JFXCheckBox("Wish To Lend");
        CheckBox sell = new JFXCheckBox("Wish To Sell");


        tableViewGenerator.getFilterGroup().addCheckBox(lend, ((entity, toMatch) -> entity.getWillingness() == Willingness.LEND))
                .addCheckBox(sell, ((entity, toMatch) -> entity.getWillingness() == Willingness.SELL));

        lend.setSelected(true);
        sell.setSelected(true);
        getPane(DashboardPane.TOP).getChildren().addAll(lend, sell);
        if (includePrivate) {
            CheckBox privateCheckBox = new JFXCheckBox("Private");
            tableViewGenerator.getFilterGroup()
                    .addCheckBox(privateCheckBox, ((entity, toMatch) -> entity.getWillingness() == Willingness.Private));
            privateCheckBox.setSelected(true);
            getPane(DashboardPane.TOP).getChildren().addAll(privateCheckBox);
        }
    }
}

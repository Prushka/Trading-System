package phase2.trade.item.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import phase2.trade.command.Command;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.editor.ItemEditor;
import phase2.trade.item.*;
import phase2.trade.item.command.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class ItemManageController extends ItemController implements Initializable {

    public ItemManageController(ControllerResources controllerResources) {
        super(controllerResources, true, true);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        Command<List<Item>> getItems = getCommandFactory().getCommand(GetAllItems::new);
        getItems.execute((result, resultStatus) -> {
            resultStatus.setSucceeded(() -> {
                setDisplayData(FXCollections.observableArrayList(result));
                afterFetch();
            });
            resultStatus.handle(getPopupFactory());
        });
    }

    private void afterFetch() {
        addNameColumn(true);
        addDescriptionColumn(true);
        addOwnershipColumn(true);
        addQuantityColumn(true);
        addPriceColumn(true);
        addCategoryColumn(true);
        addWillingnessColumn(true);
        addUIDColumn();

        addSearchName();
        addSearchDescription();
        addCategoryComboBox();
        addOwnershipComboBox();
        addWillingnessCheckBoxes();

        JFXButton deleteButton = new JFXButton("Delete");

        hookUpRemoveCommand(getCommandFactory().getCommand(RemoveItem::new, command -> command.setItemIds(idsRemoved)), Item::getUid);

        Button reviewItems = new JFXButton("Mark selected items as reviewItems");
        reviewItems.setOnAction(event -> shortenAlterOfSelected(getSelected(), Ownership.OWNER.name(), s -> {
        }, ItemEditor::alterOwnership));
        addButton(reviewItems);
        tableViewGenerator.build();
    }

    // Due to the fact that ItemEditor don't process Permission, the reload here reloads the Controller itself
    // The reason is just it's not neat for me
    // We have to call a command above to update ownership
    // This makes the tableview unable to refresh itself. So let's reload the controller itself~
    // This however will be improved in the future if I have time
    // 1. Add a Command UpdateAnyItems (with permission)
    // 2. Make Use case classes handle permission using field annotation
    public void reload() {
        getPane("centerDashboard").getChildren().setAll(getSceneManager().loadPane(ItemManageController::new));
    }

}

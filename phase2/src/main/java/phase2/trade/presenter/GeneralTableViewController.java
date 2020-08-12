package phase2.trade.presenter;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import phase2.trade.command.Command;
import phase2.trade.controller.AbstractController;
import phase2.trade.controller.ControllerProperty;
import phase2.trade.controller.ControllerResources;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class GeneralTableViewController<T> extends AbstractController implements Initializable {

    public TableView<T> tableView;

    public HBox buttons;

    protected List<Button> buttonsToDisable = new ArrayList<>();

    protected ObservableList<T> displayData;

    private final boolean ifMultipleSelection, ifEditable;

    protected TableViewGenerator<T> tableViewGenerator;

    public GeneralTableViewController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources);
        this.ifMultipleSelection = ifMultipleSelection;
        this.ifEditable = ifEditable;
    }

    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ifMultipleSelection) {
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        tableView.setEditable(ifEditable);
        tableViewGenerator = new TableViewGenerator<>(displayData, 100, tableView);
    }

    protected ObservableList<T> getSelected() {
        ObservableList<T> itemsSelected;
        itemsSelected = tableView.getSelectionModel().getSelectedItems();
        return itemsSelected;
    }


    protected void disableButtons(boolean value) {
        for (Button button : buttonsToDisable) {
            button.setDisable(value);
        }
    }

    protected Set<Long> idsRemoved = new HashSet<>();

    protected void hookUpRemoveCommand(Command<?> command, EntityIdLookUp<T> entityIdLookUp) {
        displayData.addListener((ListChangeListener<T>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    disableButtons(true);
                    idsRemoved.clear();
                    for (T entity : c.getRemoved()) {
                        idsRemoved.add(entityIdLookUp.getId(entity));
                    }
                    command.execute((result, resultStatus) -> {
                        resultStatus.setAfter(() -> disableButtons(false));
                        resultStatus.handle(getPopupFactory());
                    });
                }
            }
        });
    }

}

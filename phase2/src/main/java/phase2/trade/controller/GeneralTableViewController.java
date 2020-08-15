package phase2.trade.controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import phase2.trade.command.Command;
import phase2.trade.view.FilterPredicate;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public class GeneralTableViewController<T> extends AbstractController implements Initializable {

    @FXML
    protected TableView<T> tableView;

    @FXML
    protected HBox hBox;

    protected List<Button> buttonsToDisable = new ArrayList<>();

    protected ObservableList<T> displayData = FXCollections.observableArrayList();

    private final boolean ifMultipleSelection, ifEditable;

    protected TableViewGenerator<T> tableViewGenerator;

    public GeneralTableViewController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable) {
        super(controllerResources);
        this.ifMultipleSelection = ifMultipleSelection;
        this.ifEditable = ifEditable;
        if (getPane("topBar") != null) getPane("topBar").getChildren().clear();
    }

    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
        tableViewGenerator = new TableViewGenerator<>(displayData, tableView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ifMultipleSelection) {
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        tableView.setEditable(ifEditable);
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

    protected void addSearchField(String promptText, FilterPredicate<T, String> predicate) {
        JFXTextField textField = new JFXTextField();
        textField.setPromptText(promptText);
        textField.setLabelFloat(true);
        tableViewGenerator.getFilterGroup().addSearch(textField, predicate);
        getPane("topBar").getChildren().addAll(textField);
    }

    protected void addComboBox(ObservableList<String> observableList, String promptText, String allText, FilterPredicate<T, String> predicate) {
        JFXComboBox<String> combo = new JFXComboBox<>(observableList);
        combo.setPromptText(promptText);
        combo.getItems().add(allText);
        combo.setLabelFloat(true);
        tableViewGenerator.getFilterGroup().addComboBox(combo, predicate);
        getPane("topBar").getChildren().addAll(combo);
    }

    protected void addButton(Button... buttons){
        hBox.getChildren().addAll(buttons);
        buttonsToDisable.addAll(Arrays.asList(buttons));
    }
}

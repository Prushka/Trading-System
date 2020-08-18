package phase2.trade.controller;

import com.jfoenix.controls.JFXButton;
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
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.database.TriConsumer;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.editor.EntityIdLookUp;
import phase2.trade.view.FilterPredicate;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
// To create an AbstractTableController<T> in hierarchy
//  If AbstractEditableTableController<T,E> is a subclass of AbstractTableController<T> -> AbstractEditableController<T, E> wouldn't be in hierarchy
//  If AbstractEditableTableController<T,E> is a subclass of AbstractEditableController<T,E> -> AbstractTableController<T> wouldn't be in hierarchy
//  If composition is used, the update entity method would be impossible to customize (It will require a ResultStatusCallback)
public abstract class AbstractEditableTableController<T, E> extends EditableController<T, E> implements Initializable {

    @FXML
    protected TableView<T> tableView;

    // @FXML
    // protected VBox root;

    protected ObservableList<T> displayData = FXCollections.observableArrayList();

    private final boolean ifMultipleSelection, ifEditable;

    protected TableViewGenerator<T> tableViewGenerator;

    public AbstractEditableTableController(ControllerResources controllerResources,
                                           boolean ifMultipleSelection, boolean ifEditable,
                                           EditorSupplier<E, T> supplier) {
        super(controllerResources, supplier);
        this.ifMultipleSelection = ifMultipleSelection;
        this.ifEditable = ifEditable;
    }

    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
        tableViewGenerator = new TableViewGenerator<>(displayData, tableView);
    }

    public void setDisplayData(Collection<T> displayData) {
        setDisplayData(FXCollections.observableArrayList(displayData));
    }

    public void reloadNewDisplayData(Collection<T> displayData) {
        tableView.setItems(FXCollections.observableArrayList(displayData));
    }

    protected List<Long> getSelectedEntityIds(EntityIdLookUp<T> entityIdLookUp) {
        List<Long> ids = new ArrayList<>();
        getSelected().forEach(entityIdLookUp::getId);
        return ids;
    }

    protected void shortenAlter(String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        shortenAlterOfSelected(getSelected(), newValue, statusCallback, consumer);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ifMultipleSelection) {
            tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
        tableView.setEditable(ifEditable);
    }

    public ObservableList<T> getSelected() {
        ObservableList<T> itemsSelected = tableView.getSelectionModel().getSelectedItems();
        if (itemsSelected.size() == 0) {
            nothingSelectedToast();
        }
        return itemsSelected;
    }


    protected void nothingSelectedToast() {
        getNotificationFactory().toast(3, "You didn't select anything", "CLOSE");
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
                        resultStatus.handle(getNotificationFactory());
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
        getPane(DashboardPane.TOP).getChildren().addAll(textField);
    }

    protected void addComboBox(ObservableList<String> observableList, String promptText, String allText, FilterPredicate<T, String> predicate) {
        JFXComboBox<String> combo = new JFXComboBox<>(observableList);
        combo.setPromptText(promptText);
        combo.getItems().add(allText);
        combo.setLabelFloat(true);
        tableViewGenerator.getFilterGroup().addComboBox(combo, predicate);
        getPane(DashboardPane.TOP).getChildren().addAll(combo);
    }

    // This reloads the entire controller. Thus, the new data is retrieved from database
    // Please use the pub/sub to reload (which will reflect changes in multiple clients automatically)
    @Deprecated
    protected void addReloadButton(ControllerSupplier<?> controllerSupplier) {
        Button button = new JFXButton("reload");
        button.setOnAction(event -> getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controllerSupplier)));
        getPane(DashboardPane.TOP).getChildren().addAll(button);
    }

    public TableView<T> getTableView() {
        return tableView;
    }
}

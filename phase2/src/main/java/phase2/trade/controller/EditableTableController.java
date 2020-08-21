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
import phase2.trade.editor.Editor;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.editor.EntityIdLookUp;
import phase2.trade.view.FilterPredicate;
import phase2.trade.view.TableViewGenerator;

import java.net.URL;
import java.util.*;

/**
 * The base controller for controllers that contain a TableView.<p>
 * The {@link TableViewGenerator} is included to allow easy access to {@link phase2.trade.view.FilterGroup}.<p>
 * It also helps with adding editable/non-editable columns using either field name or a custom CellFactory and CellValueFactory.<p>
 * The subclasses are responsible for building the {@link TableViewGenerator} once they finished adding columns and search filters.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_table_view.fxml")
// To create a ListController<T> in hierarchy
//  If EditableTableController<T,E> is a subclass of ListController<T> -> EditableController<T, E> wouldn't be in hierarchy
//  If ListController<T,E> is a subclass of EditableController<T,E> -> ListController<T> wouldn't be in hierarchy
//  Composition may be considered in the future.
public abstract class EditableTableController<T, E extends Editor<T>> extends EditableController<T, E> implements Initializable {

    /**
     * The Table view.
     */
    @FXML
    protected TableView<T> tableView;

    /**
     * The Display data.
     */
    protected ObservableList<T> displayData = FXCollections.observableArrayList();

    private final boolean ifMultipleSelection, ifEditable;

    /**
     * The Table view generator.
     */
    protected TableViewGenerator<T> tableViewGenerator;

    /**
     * Constructs a new Editable table controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     * @param ifEditable          the if editable
     * @param supplier            the supplier
     */
    public EditableTableController(ControllerResources controllerResources,
                                   boolean ifMultipleSelection, boolean ifEditable,
                                   EditorSupplier<E, T> supplier) {
        super(controllerResources, supplier);
        this.ifMultipleSelection = ifMultipleSelection;
        this.ifEditable = ifEditable;
    }

    /**
     * Sets display data.
     *
     * @param displayData the display data
     */
    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
        tableViewGenerator = new TableViewGenerator<>(displayData, tableView);
    }

    /**
     * Sets display data.
     *
     * @param displayData the display data
     */
    public void setDisplayData(Collection<T> displayData) {
        setDisplayData(FXCollections.observableArrayList(displayData));
    }

    /**
     * Reloads new display data.
     *
     * @param displayData the display data
     */
    public void reloadNewDisplayData(Collection<T> displayData) {
        tableView.setItems(FXCollections.observableArrayList(displayData));
    }

    /**
     * Gets selected entity ids.
     *
     * @param entityIdLookUp the entity id look up
     * @return the selected entity ids
     */
    protected List<Long> getSelectedEntityIds(EntityIdLookUp<T> entityIdLookUp) {
        List<Long> ids = new ArrayList<>();
        getSelected().forEach(entityIdLookUp::getId);
        return ids;
    }

    /**
     * This method allows an alter method to be defined for an editing operation upon all items selected in this TableView.<p>
     *
     * @param newValue       the new value
     * @param statusCallback the status callback
     * @param consumer       the consumer used to invoke the editor's method and call the {@link StatusCallback}
     */
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

    /**
     * Gets selected items from the TableView.
     *
     * @return the selected
     */
    public ObservableList<T> getSelected() {
        ObservableList<T> itemsSelected = tableView.getSelectionModel().getSelectedItems();
        if (itemsSelected.size() == 0) {
            nothingSelectedToast();
        }
        return itemsSelected;
    }


    /**
     * Toast a nothing selected message.
     */
    protected void nothingSelectedToast() {
        getNotificationFactory().toast(3, "You didn't select anything", "CLOSE");
    }

    /**
     * The Ids removed.
     */
    protected Set<Long> idsRemoved = new HashSet<>();

    /**
     * Hook up remove command.
     *
     * @param command        the command
     * @param entityIdLookUp the entity id look up
     */
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

    /**
     * Add search field.
     *
     * @param promptText the prompt text
     * @param predicate  the predicate
     */
    protected void addSearchField(String promptText, FilterPredicate<T, String> predicate) {
        JFXTextField textField = new JFXTextField();
        textField.setPromptText(promptText);
        textField.setLabelFloat(true);
        tableViewGenerator.getFilterGroup().addSearch(textField, predicate);
        getPane(DashboardPane.TOP).getChildren().addAll(textField);
    }

    /**
     * Add combo box to filter the TableView.
     *
     * @param observableList the observable list
     * @param promptText     the prompt text
     * @param allText        the all text
     * @param predicate      the predicate
     */
    protected void addComboBox(ObservableList<String> observableList, String promptText, String allText, FilterPredicate<T, String> predicate) {
        JFXComboBox<String> combo = new JFXComboBox<>(observableList);
        combo.setPromptText(promptText);
        combo.getItems().add(allText);
        combo.setLabelFloat(true);
        tableViewGenerator.getFilterGroup().addComboBox(combo, predicate);
        getPane(DashboardPane.TOP).getChildren().addAll(combo);
    }

    /**
     * Add reload button to manually reload the view and data, replaced by the pub-sub.
     *
     * @param controllerSupplier the controller supplier
     */
    // This reloads the entire controller. Thus, the new data is retrieved from database
    // Please use the pub/sub to reload (which will reflect changes in multiple clients automatically)
    @Deprecated
    protected void addReloadButton(ControllerSupplier<?> controllerSupplier) {
        Button button = new JFXButton("reload");
        button.setOnAction(event -> getPane(DashboardPane.CENTER).getChildren().setAll(getSceneManager().loadPane(controllerSupplier)));
        getPane(DashboardPane.TOP).getChildren().addAll(button);
    }

    /**
     * Gets table view.
     *
     * @return the table view
     */
    public TableView<T> getTableView() {
        return tableView;
    }
}

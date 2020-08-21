package phase2.trade.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import phase2.trade.view.ListViewGenerator;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

/**
 * The base controller for controllers that contain a ListView.<p>
 * The {@link ListViewGenerator} is included to allow easy access to {@link phase2.trade.view.FilterGroup}.<p>
 * The subclasses are responsible for building the {@link ListViewGenerator} once they finished adding columns and search filters.<p>
 * No edit actions are expected directly using the ListView. Subclasses should use other view nodes to perfom their own actions.
 *
 * @param <T> the type of the class that will be displayed
 * @author Dan Lyu
 */
@ControllerProperty(viewFile = "general_list_view.fxml")
// Since AbstractEditableTableController may be editable. It needs to extend EditableController
// However this ListController is not supposed to be editable. These two controllers won't be able to share one base
public abstract class ListController<T> extends AbstractController implements Initializable {

    /**
     * The List view.
     */
    @FXML
    protected ListView<T> listView;

    private final boolean ifMultipleSelection;

    /**
     * The List view generator.
     */
    protected ListViewGenerator<T> listViewGenerator;

    /**
     * The Display data.
     */
    protected ObservableList<T> displayData = FXCollections.observableArrayList();

    /**
     * Constructs a new Abstract list controller.
     *
     * @param controllerResources the controller resources
     * @param ifMultipleSelection the if multiple selection
     */
    public ListController(ControllerResources controllerResources, boolean ifMultipleSelection) {
        super(controllerResources);
        this.ifMultipleSelection = ifMultipleSelection;
    }

    /**
     * Sets display data of the List.
     *
     * @param displayData the display data
     */
    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
        listViewGenerator = new ListViewGenerator<>(displayData, listView);
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
     * Reload new display data.
     *
     * @param displayData the display data
     */
    public void reloadNewDisplayData(Collection<T> displayData) {
        listView.setItems(FXCollections.observableArrayList(displayData));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ifMultipleSelection) {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }
}

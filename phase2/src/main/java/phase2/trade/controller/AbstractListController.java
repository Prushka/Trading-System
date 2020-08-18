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

@ControllerProperty(viewFile = "general_list_view.fxml")
// Since AbstractEditableTableController may be editable. It needs to extend EditableController
// However this ListController is not supposed to be editable. These two controllers won't be able to share one base
public abstract class AbstractListController<T> extends AbstractController implements Initializable {

    @FXML
    protected ListView<T> listView;

    // @FXML
    // protected VBox root;

    private boolean ifMultipleSelection;

    protected ListViewGenerator<T> listViewGenerator;

    protected ObservableList<T> displayData = FXCollections.observableArrayList();

    public AbstractListController(ControllerResources controllerResources, boolean ifMultipleSelection) {
        super(controllerResources);
        this.ifMultipleSelection = ifMultipleSelection;
    }

    public void setDisplayData(ObservableList<T> displayData) {
        this.displayData = displayData;
        listViewGenerator = new ListViewGenerator<>(displayData, listView);
    }

    public void setDisplayData(Collection<T> displayData) {
        setDisplayData(FXCollections.observableArrayList(displayData));
    }

    public void reloadNewDisplayData(Collection<T> displayData) {
        listView.setItems(FXCollections.observableArrayList(displayData));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (ifMultipleSelection) {
            listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }

    protected void nothingSelectedToast() {
        getNotificationFactory().toast(3, "You didn't select anything", "CLOSE");
    }
}

package phase2.trade.view;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;


public class ListViewGenerator<T> {

    /*
    private final ListView<T> ListView;

    private final List<T> listOfElements = new ArrayList<>();

    private final FilteredList<T> filteredList;

    public ListViewGenerator(ListView<T> ListView) {
        filteredList = new FilteredList<>(listOfElements, p -> true);
        this.ListView = ListView;
    }

    public ListViewGenerator() {
        this(new ListView<>());
    }

    private TableColumn<T, String> getTableColumn(String name, String fieldName, int minWidth) {
        TableColumn<T, String> column = new TableColumn<>(name);
        column.setMinWidth(minWidth);
        column.setCellValueFactory(new PropertyValueFactory<>(fieldName));
        return column;
    }


    public ListViewGenerator<T> addElement(T element) {
        listOfElements.add(element);
        return this;
    }


    public ListViewGenerator<T> addSearch(TextField textField, TextFieldPredicate<T> predicate) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredList.setPredicate(t -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                return predicate.test(t, textField.getText());
            });
        });
        return this;
    }

    public ListView<T> build() {
        SortedList<T> sortedList = new SortedList<>(filteredList);
        ListView.setItems(sortedList);
        return ListView;
    }*/
}

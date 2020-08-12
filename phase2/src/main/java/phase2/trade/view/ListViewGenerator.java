package phase2.trade.view;

import javafx.collections.FXCollections;
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

    private final ObservableList<T> original;

    private final ListView<T> ListView;

    private final FilteredList<T> filteredList;

    private final FilterGroup<T> filterGroup;

    public ListViewGenerator(ObservableList<T> original, ListView<T> ListView) {
        filteredList = new FilteredList<>(original, p -> true);
        this.ListView = ListView;
        this.original = original;
        filterGroup = new FilterGroup<>(filteredList);
    }

    public ListViewGenerator(ObservableList<T> original) {
        this(original, new ListView<>());
    }

    public ListViewGenerator(ListView<T> listView) {
        this(FXCollections.observableArrayList(), listView);
    }


    public ListViewGenerator<T> addElement(T element) {
        original.add(element);
        return this;
    }

    public ListView<T> build() {
        filterGroup.group();
        SortedList<T> sortedList = new SortedList<>(filteredList);
        ListView.setItems(sortedList);
        return ListView;
    }

    public FilterGroup<T> getFilterGroup() {
        return filterGroup;
    }
}

package phase2.trade.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.ListView;


/**
 * The List view generator.
 *
 * @param <T> the entity type of the ListView
 * @author Dan Lyu
 */
public class ListViewGenerator<T> {

    private final ObservableList<T> original;

    private final ListView<T> ListView;

    private final FilteredList<T> filteredList;

    private final FilterGroup<T> filterGroup;

    /**
     * Constructs a new List view generator.
     *
     * @param original the original
     * @param ListView the list view
     */
    public ListViewGenerator(ObservableList<T> original, ListView<T> ListView) {
        filteredList = new FilteredList<>(original, p -> true);
        this.ListView = ListView;
        this.original = original;
        filterGroup = new FilterGroup<>(filteredList);
    }

    /**
     * Constructs a new List view generator.
     *
     * @param original the original
     */
    public ListViewGenerator(ObservableList<T> original) {
        this(original, new ListView<>());
    }

    /**
     * Constructs a new List view generator.
     *
     * @param listView the list view
     */
    public ListViewGenerator(ListView<T> listView) {
        this(FXCollections.observableArrayList(), listView);
    }


    /**
     * Add element list view generator.
     *
     * @param element the element
     * @return the list view generator
     */
    public ListViewGenerator<T> addElement(T element) {
        original.add(element);
        return this;
    }

    /**
     * Build list view.
     *
     * @return the list view
     */
    public ListView<T> build() {
        filterGroup.group();
        SortedList<T> sortedList = new SortedList<>(filteredList);
        ListView.setItems(sortedList);
        return ListView;
    }

    /**
     * Gets filter group.
     *
     * @return the filter group
     */
    public FilterGroup<T> getFilterGroup() {
        return filterGroup;
    }
}

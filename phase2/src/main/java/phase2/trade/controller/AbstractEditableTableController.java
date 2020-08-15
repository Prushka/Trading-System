package phase2.trade.controller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import phase2.trade.callback.StatusCallback;
import phase2.trade.database.TriConsumer;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.editor.EntityIdLookUp;

import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public abstract class AbstractEditableTableController<T, E> extends AbstractTableController<T> implements Initializable {

    private final EditorSupplier<E, T> supplier;

    private final EntityIdLookUp<T> entityIdLookUp;

    public AbstractEditableTableController(ControllerResources controllerResources, boolean ifMultipleSelection, boolean ifEditable,
                                           EditorSupplier<E, T> supplier, EntityIdLookUp<T> entityIdLookUp) {
        super(controllerResources, ifMultipleSelection, ifEditable);
        this.supplier = supplier;
        this.entityIdLookUp = entityIdLookUp;
    }

    private List<T> getEntityAsList(T entity) {
        return new ArrayList<T>() {{
            add(entity);
        }};
    }

    protected void shortenAlter(T entity, String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        consumer.consume(supplier.get(getEntityAsList(entity), getConfigBundle()), newValue, statusCallback);
        updateEntity(entity);
    }

    protected void shortenAlterOfSelected(String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        consumer.consume(supplier.get(getSelected(), getConfigBundle()), newValue, statusCallback);
        updateEntity(getSelected());
    }

    protected void updateEntity(T entity) {
        List<T> items = new ArrayList<>();
        items.add(entity);
        updateEntity(items);
    }

    protected abstract void updateEntity(List<T> entities);

    protected void nothingSelectedToast() {
        getPopupFactory().toast(3, "You didn't select anything", "CLOSE");
    }

    @Override
    protected ObservableList<T> getSelected() {
        if (super.getSelected().size() == 0) {
            nothingSelectedToast();
        }
        return super.getSelected();
    }

    protected List<Long> getSelectedEntityIds() {
        List<Long> ids = new ArrayList<>();
        getSelected().forEach(entityIdLookUp::getId);
        return ids;
    }

}

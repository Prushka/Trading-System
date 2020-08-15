package phase2.trade.controller;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.database.TriConsumer;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.editor.EntityIdLookUp;

import java.net.URL;
import java.util.*;

@ControllerProperty(viewFile = "general_table_view.fxml")
public abstract class EditableController<T, E> extends AbstractController {

    private final EditorSupplier<E, T> supplier;


    protected Command<?> updateEntityCommand;

    public EditableController(ControllerResources controllerResources,
                              EditorSupplier<E, T> supplier) {
        super(controllerResources);
        this.supplier = supplier;
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

    protected void shortenAlterOfSelected(List<T> selected, String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        consumer.consume(supplier.get(selected, getConfigBundle()), newValue, statusCallback);
        updateEntity(selected);
    }

    private void updateEntity(T entity) {
        List<T> temp = new ArrayList<>();
        temp.add(entity);
        updateEntity(temp);
    }

    protected abstract void updateEntity(List<T> entities);

    public void setUpdateEntityCommand(Command<?> updateEntityCommand) {
        this.updateEntityCommand = updateEntityCommand;
    }
}

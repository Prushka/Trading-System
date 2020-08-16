package phase2.trade.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import phase2.trade.callback.StatusCallback;
import phase2.trade.command.Command;
import phase2.trade.database.TriConsumer;
import phase2.trade.editor.EditorSupplier;
import phase2.trade.editor.EntityIdLookUp;

import java.net.URL;
import java.util.*;

public abstract class EditableController<T, E> extends AbstractController {

    private final EditorSupplier<E, T> supplier;

    protected List<Button> buttonsToDisable = new ArrayList<>();

    @FXML
    protected Pane buttonPane;

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

    protected void updateEntity(T entity) {
        List<T> temp = new ArrayList<>();
        temp.add(entity);
        updateEntity(temp);
    }

    protected abstract void updateEntity(List<T> entities);

    protected void disableButtons(boolean value) {
        for (Button button : buttonsToDisable) {
            button.setDisable(value);
        }
    }

    protected void addButton(Button... buttons) {
        buttonPane.getChildren().addAll(buttons);
        buttonsToDisable.addAll(Arrays.asList(buttons));
    }

}

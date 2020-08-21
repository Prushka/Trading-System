package phase2.trade.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import phase2.trade.callback.StatusCallback;
import phase2.trade.database.TriConsumer;
import phase2.trade.editor.Editor;
import phase2.trade.editor.EditorSupplier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The base controller for controllers that require an {@link Editor} to edit an entity.<p>
 * The {@link Editor} is often used together with an {@link phase2.trade.command.UpdateCommand} to directly update an entity in the gateway.
 *
 * @param <T> the entity type to be edited
 * @param <E> the editor type used to edit this entity
 * @author Dan Lyu
 */
public abstract class EditableController<T, E extends Editor<T>> extends AbstractController {

    private final EditorSupplier<E, T> supplier;

    /**
     * The Buttons to disable once the execution happens (so that users won't be able to slap the buttons when the (database) is executing in another thread).
     */
    protected List<Button> buttonsToDisable = new ArrayList<>();

    /**
     * The Button pane.
     */
    @FXML
    protected Pane buttonPane;

    /**
     * Constructs a new Editable controller.
     *
     * @param controllerResources the controller resources
     * @param supplier            the supplier
     */
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

    /**
     * This method allows an alter method to be defined for an editing operation upon a given entity.<p>
     * This method is often used in a TableView to allow users to edit a column directly. The corresponding method in the {@link Editor} will be invoked and the statusCallback will be used.
     *
     * @param entity         the entity
     * @param newValue       the new value
     * @param statusCallback the status callback
     * @param consumer       the consumer
     */
    protected void shortenAlter(T entity, String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        consumer.consume(supplier.get(getEntityAsList(entity), getConfigBundle()), newValue, statusCallback);
        updateEntity(entity);
    }

    /**
     * This method allows an alter method to be defined for an editing operation upon a collection of entities.<p>
     *
     * @param selected       the selected
     * @param newValue       the new value
     * @param statusCallback the status callback
     * @param consumer       the consumer
     */
    protected void shortenAlterOfSelected(List<T> selected, String newValue, StatusCallback statusCallback, TriConsumer<E, String, StatusCallback> consumer) {
        consumer.consume(supplier.get(selected, getConfigBundle()), newValue, statusCallback);
        updateEntity(selected);
    }

    /**
     * Update a single entity. It overloads the abstract method of updating a collection of entities.
     * That overloaded method is expected to be overridden in subclasses using {@link phase2.trade.command.UpdateCommand}
     *
     * @param entity the entity
     */
    protected void updateEntity(T entity) {
        List<T> temp = new ArrayList<>();
        temp.add(entity);
        updateEntity(temp);
    }

    /**
     * Update a collection of entities using Commands with Gateway involved.
     *
     * @param entities the entities
     */
    protected abstract void updateEntity(List<T> entities);

    /**
     * Disable buttons.
     *
     * @param value the value
     */
    protected void disableButtons(boolean value) {
        for (Button button : buttonsToDisable) {
            button.setDisable(value);
        }
    }

    /**
     * Add buttons to the pane and to the disable collection.
     *
     * @param buttons the buttons
     */
    protected void addButton(Button... buttons) {
        buttonPane.getChildren().addAll(buttons);
        buttonsToDisable.addAll(Arrays.asList(buttons));
    }

}

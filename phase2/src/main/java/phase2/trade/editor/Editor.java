package phase2.trade.editor;

import phase2.trade.config.ConfigBundle;

import java.util.ArrayList;
import java.util.List;

/**
 * The base Editor class.
 *
 * @param <T> the type of the entities to be edited
 * @author Dan Lyu
 */
public class Editor<T> {

    /**
     * The Entities.
     */
    protected final List<T> entities;

    /**
     * The Config bundle.
     */
    protected final ConfigBundle configBundle;

    /**
     * Constructs a new Editor.
     *
     * @param entities     the entities
     * @param configBundle the config bundle
     */
    public Editor(List<T> entities, ConfigBundle configBundle) {
        this.entities = entities;
        this.configBundle = configBundle;
    }

    private List<T> getEntities(T entity) {
        List<T> entities = new ArrayList<>();
        entities.add(entity);
        return entities;
    }

    /**
     * Constructs a new Editor.
     *
     * @param entity       the entity
     * @param configBundle the config bundle
     */
    public Editor(T entity, ConfigBundle configBundle) {
        this.entities = getEntities(entity);
        this.configBundle = configBundle;
    }

    /**
     * If a given String is an integer.
     *
     * @param toExam the String to exam
     * @return <code>true</code> if the given String can be parsed to an integer
     */
    protected boolean isInt(String toExam) {
        return toExam.matches("[0-9]+");
    }

    /**
     * If a given String is a number.
     *
     * @param toExam the String to exam
     * @return <code>true</code> if the given String can be parsed to a number
     */
    protected boolean isNumber(String toExam) {
        return toExam.matches("-?\\d+(\\.\\d+)?");
    }
}

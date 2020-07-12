package group.repository;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The Iterator that takes in a filter.<p>
 * The {@link #hasNext()} method will match the element to the condition.
 *
 * @param <T> The entity the iterator iterates on
 * @author Dan Lyu
 */
public class RepositoryIterator<T> implements Iterator<T> {

    /**
     * The list of entities this iterator iterates on.
     */
    private final List<T> data;

    /**
     * The current pointer
     */
    private int current;

    /**
     * The filter that's used to if the element meets the condition
     */
    private final Filter<T> filter;

    /**
     * @param data   the entity list to be iterated on
     * @param filter the filter used to match results
     */
    public RepositoryIterator(List<T> data, Filter<T> filter) {
        this.data = data;
        this.current = 0;
        this.filter = filter;
    }

    /**
     * @return if the list has a next element that meets the {@link #filter} condition
     */
    @Override
    public boolean hasNext() {
        if (current < data.size()) {
            if (!filter.match(data.get(current))) {
                current++;
                return hasNext();
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the entity that matches the filter
     */
    @Override
    public T next() {
        T res;
        try {
            res = data.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }
}

package group.repository;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class RepositoryIterator<T> implements Iterator<T> {

    // lecture code

    private final List<T> data;
    private int current;
    private final Filter<T> filter;

    public RepositoryIterator(List<T> data, Filter<T> filter) {
        this.data = data;
        this.current = 0;
        this.filter = filter;
    }

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

    @Override
    public T next() {
        T res;

        // List.get(i) throws an IndexOutBoundsException if
        // we call it with i >= contacts.size().
        // But Iterator's next() needs to throw a
        // NoSuchElementException if there are no more elements.
        try {
            res = data.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        current += 1;
        return res;
    }
}

package repository;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class RepositoryIterator<T> implements Iterator<T> {

    // lecture code, need to better implement

    private final List<T> data;
    private int current;

    public RepositoryIterator(List<T> data) {
        this.data = data;
        this.current = 0;
    }

    @Override
    public boolean hasNext() {
        return current < data.size();
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

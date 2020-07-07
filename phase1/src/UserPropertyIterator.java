//package system;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class UserPropertyIterator implements Iterator<String> {
    private List<String> properties = new ArrayList<>();
    private int curr;

    public UserPropertyIterator() {
        properties.add("Enter your desired username: ");
        properties.add("Enter you email address: ");
        properties.add("Enter your telephone number: ");
        properties.add("Enter your password: ");
    }

    @Override
    public boolean hasNext() {
        return properties.size() > curr;
    }

    @Override
    public String next() {
        String nxt;
        try {
            nxt = properties.get(curr);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
        curr += 1;
        return nxt;
    }


}

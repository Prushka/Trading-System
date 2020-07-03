package notification.support;

import repository.Mappable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ticket implements Serializable, Mappable {

    private static final List<String> header = Arrays.asList("content", "category");

    @Override
    public List<String> getHeader() {
        return header;
    }

    @Override
    public List<String> toList() {
        return Arrays.asList(content, category.toString());
    }

    public enum Category {
        ACCOUNT, TRADE
    }

    public enum Emergency {
        LOW(0), MEDIUM(1), HIGH(2);

        int id;

        Emergency(int id) {
            this.id = id;
        }
    }

    private Category category;

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    private Emergency emergency;

    private String submitterEmail;

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public Ticket(String content, Category category) {
        this.content = content;
        this.category = category;
    }

    public Ticket(List<String> data) {
        this.content = data.get(0);
        this.category = Category.valueOf(data.get(1));
    }
}

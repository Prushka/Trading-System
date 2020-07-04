package admin;

import menu.data.Request;
import repository.Mappable;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Ticket implements Serializable, Mappable {

    private Category category;

    private Priority priority;

    private State state;

    private String submitterEmail;

    private String content;

    private long timeStamp;

    public Ticket(String content, Category category) {
        this.content = content;
        this.category = category;
    }

    @Override
    public List<String> toList() { // order matters
        return Arrays.asList(
                submitterEmail,
                content,
                category.toString(),
                state.toString(),
                String.valueOf(timeStamp),
                priority.toString());
    }

    public Ticket(Request request) { // order doesn't matter
        this.submitterEmail = request.get("user.email");
        this.content = request.get("content");
        this.category = Category.getById(Integer.parseInt(request.get("category")));
        this.state = State.PENDING_ALLOCATION;
        this.timeStamp = request.getTimeStamp();
        this.priority = Priority.getById(Integer.parseInt(request.get("priority")));
    }

    public Ticket(List<String> data) { // order matters TODO: decouple
        this.submitterEmail = data.get(0);
        this.content = data.get(1);
        this.category = Category.valueOf(data.get(2));
        this.state = State.valueOf(data.get(3));
        this.timeStamp = Long.parseLong(data.get(4));
        this.priority = Priority.valueOf(data.get(5));
    }

    public enum Category {
        UNKNOWN(-1), ACCOUNT(0), TRADE(1), BOOK(2);

        int id;

        Category(int id) {
            this.id = id;
        }

        public static Category getById(int id) {
            for (Category value : values()) {
                if (value.id == id) return value;
            }
            return UNKNOWN;
        }
    }

    public enum Priority {
        LOW(0), MEDIUM(1), HIGH(2);

        int id;

        Priority(int id) {
            this.id = id;
        }

        public static Priority getById(int id) {
            for (Priority value : values()) {
                if (value.id == id) return value;
            }
            return LOW;
        }
    }

    public enum State {
        PENDING_ALLOCATION, FEEDBACK, SOLVED, CLOSED
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}

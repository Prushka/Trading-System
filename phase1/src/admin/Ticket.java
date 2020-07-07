package admin;

import menu.data.Request;
import repository.map.EntityMappable;
import repository.UniqueId;
import repository.map.MappableBase;

import java.io.Serializable;
import java.util.List;

public class Ticket extends MappableBase implements Serializable, EntityMappable, UniqueId {

    private Category category;

    private Priority priority;

    private State state; 

    private String submitterEmail;

    private String content;

    private Long timeStamp; // they are nullable

    private Long uid; // it's a list to be serialized. maybe uid = index?

    public Ticket(List<String> data) {
        super(data);
    }

    public Ticket(String content, Category category, long timeStamp) {
        this.content = content;
        this.category = category;
        this.timeStamp = timeStamp;
    }

    public Ticket(Request request, Long uid) { // order doesn't matter
        this.submitterEmail = request.get("user.email");
        this.content = request.get("content");
        this.category = Category.getById(Integer.parseInt(request.get("category")));
        this.state = State.PENDING_ALLOCATION;
        this.timeStamp = request.getTimeStamp();
        this.priority = Priority.getById(Integer.parseInt(request.get("priority")));
        this.uid = uid;
    }

    public enum Category {
        UNDEFINED(-1), ACCOUNT(0), TRADE(1), BOOK(2);

        int id;

        Category(int id) {
            this.id = id;
        }


        public static Category getById(int id) {
            for (Category value : values()) {
                if (value.id == id) return value;
            }
            return UNDEFINED;
        }
    }

    public enum Priority {
        UNDEFINED(-1), LOW(0), MEDIUM(1), HIGH(2);

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

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getUid() {
        return uid;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Ticket) {
            Ticket other = (Ticket) obj;
            return this.content.equals(other.content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}

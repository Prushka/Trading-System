package group.notification;

import group.menu.data.Request;
import group.repository.UniqueId;
import group.repository.reflection.CSVMappable;
import group.repository.reflection.MappableBase;

import java.io.Serializable;
import java.util.List;

public class SupportTicket extends MappableBase implements Serializable, CSVMappable, UniqueId {

    private Category category;

    private Priority priority;

    private State state;

    private String submitterEmail;

    private String content;

    private Long timeStamp; // they are nullable

    private Integer uid; // it's a list to be serialized. maybe uid = index?

    public SupportTicket(List<String> data) {
        super(data);
    }

    public SupportTicket(Request request) {
        this.submitterEmail = request.get("user.email");
        this.content = request.get("content");
        this.category = Category.valueOf(request.get("category"));
        this.state = State.PENDING_ALLOCATION;
        this.timeStamp = request.getTimeStamp();
        this.priority = Priority.valueOf(request.get("priority"));
    }

    public enum Category {
        UNDEFINED(-1), ACCOUNT(0), TRADE(1), BOOK(2);

        final int id;

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

        final int id;

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

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public State getState() {
        return state;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof SupportTicket) {
            SupportTicket other = (SupportTicket) obj;
            return this.content.equals(other.content);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}

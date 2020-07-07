package admin;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import menu.data.Request;
import repository.EntityMappable;
import repository.UniqueId;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

public class Ticket implements Serializable, EntityMappable, UniqueId {

    private Category category;

    private Priority priority;

    private State state;

    private String submitterEmail;

    private String content;

    private Long timeStamp;

    private Long uid; // it's a list to be serialized. maybe uid = index?

    public Ticket(String content, Category category, long timeStamp) {
        this.content = content;
        this.category = category;
        this.timeStamp = timeStamp;
    }

    public String toCSVHeader() {
        StringBuilder value = new StringBuilder();
        Arrays.asList(this.getClass().getDeclaredFields()).forEach(field -> value.append(field.getName()).append(","));
        value.setLength(value.length() - 1);
        value.append("\n");
        return value.toString();
    }

    @Override
    public String toCSVString() {
        // I suppose java serialize objects in a way that constructors are not invoked.
        // Doing this will allow private members to be accessed and set
        // Fields are not in order seemingly
        // TODO: sort & most importantly do we need order
        StringBuilder value = new StringBuilder();
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (!Modifier.isTransient(field.getModifiers())) {

                    Object obj = field.get(this);
                    if (obj == null) {
                        value.append("null");
                    } else if (obj instanceof Enum) {
                        value.append(((Enum<?>) obj).name());
                    } else {
                        value.append(obj.toString());
                    }
                    value.append(",");
                }
            } catch (IllegalAccessException | NullPointerException e) {
                //TODO: implement better error handling
                value.append("null");
                e.printStackTrace();
            }
        }
        System.out.println(value.substring(0, value.length() - 1));
        return value.substring(0, value.length() - 1);
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

    public Ticket(List<String> data) { // order matters TODO: decouple
        int id = 0;
        for (Field field : this.getClass().getDeclaredFields()) {
            String representation = data.get(id); // the String representation
            Object obj = null; // declaring within the smallest scope. https://stackoverflow.com/questions/8803674/declaring-variables-inside-or-outside-of-a-loop
            try {
                // what's the difference between == and isAssignedFrom() and isAssignableFrom() from the class?
                if (representation.equals("null")) {
                    obj = null;
                } else if (field.getType().isEnum()) { // if field is an Enum, how do I put the String back?
                    obj = Enum.valueOf((Class<Enum>) field.getType(), representation);
                } else if (Integer.class.isAssignableFrom(field.getType())) {
                    obj = Integer.parseInt(representation);
                } else if (Long.class.isAssignableFrom(field.getType())) {
                    obj = Long.parseLong(representation);
                } else if (Boolean.class.isAssignableFrom(field.getType())) {
                    obj = Boolean.parseBoolean(representation);
                } else if (Double.class.isAssignableFrom(field.getType())) {
                    obj = Double.parseDouble(representation);
                } else if (Float.class.isAssignableFrom(field.getType())) {
                    obj = Float.parseFloat(representation);
                } else if (String.class.isAssignableFrom(field.getType())) {
                    obj = representation;
                }
                field.set(this, obj);
                // System.out.printf(field.getName() + ":" + representation + ", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            id++;
        }
        // System.out.println("");
        // System.out.println(content);
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

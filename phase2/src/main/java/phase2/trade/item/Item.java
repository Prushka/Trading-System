package phase2.trade.item;


import phase2.trade.user.User;

import javax.persistence.*;
import java.util.Collection;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String name;
    private String description;
    private Category category;

    private Ownership ownership;

    private Willingness willingness = Willingness.Private;

    private int quantity = 1;

    private double price = -1;

    @OneToOne
    private User owner;

    @ElementCollection
    private Collection<Tag> tags;

    public Long getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    public Willingness getWillingness() {
        return willingness;
    }

    public void setWillingness(Willingness willingness) {
        this.willingness = willingness;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public boolean belongsTo(User user) {
        return getOwner().getUid().equals(user.getUid());
    }
}

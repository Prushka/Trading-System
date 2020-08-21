package phase2.trade.item;


import phase2.trade.user.User;

import javax.persistence.*;
import java.util.Collection;

/**
 * The Item.
 *
 * @author Dan Lyu
 * @author danyal
 */
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

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets category.
     *
     * @return the category
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Sets category.
     *
     * @param category the category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * Gets ownership.
     *
     * @return the ownership
     */
    public Ownership getOwnership() {
        return ownership;
    }

    /**
     * Sets ownership.
     *
     * @param ownership the ownership
     */
    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

    /**
     * Gets willingness.
     *
     * @return the willingness
     */
    public Willingness getWillingness() {
        return willingness;
    }

    /**
     * Sets willingness.
     *
     * @param willingness the willingness
     */
    public void setWillingness(Willingness willingness) {
        this.willingness = willingness;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public Collection<Tag> getTags() {
        return tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags
     */
    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    /**
     * Belongs to boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public boolean belongsTo(User user) {
        return getOwner().getUid().equals(user.getUid());
    }
}

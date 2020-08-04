package phase2.trade.item;


import phase2.trade.user.User;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String name;
    private String description;
    private String category;

    private Ownership ownership;

    private Willingness willingness;

    @ManyToOne
    private User owner;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        owner.addItem(this);
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Ownership getOwnership() {
        return ownership;
    }

    public void setOwnership(Ownership ownership) {
        this.ownership = ownership;
    }

}

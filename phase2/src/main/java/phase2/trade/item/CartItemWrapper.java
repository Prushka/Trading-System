package phase2.trade.item;


import javax.persistence.*;

/**
 * The Cart item wrapper.
 *
 * @author Dan Lyu
 */
@Entity
public class CartItemWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    private Item item;

    private int quantity;

    /**
     * Constructs a new Cart item wrapper.
     *
     * @param item     the item
     * @param quantity the quantity
     */
    public CartItemWrapper(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    /**
     * Constructs a new Cart item wrapper.
     */
    public CartItemWrapper() {

    }

    /**
     * Gets uid.
     *
     * @return the uid
     */
    public Long getUid() {
        return uid;
    }

    /**
     * Sets uid.
     *
     * @param uid the uid
     */
    public void setUid(Long uid) {
        this.uid = uid;
    }

    /**
     * Gets item.
     *
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    /**
     * Sets item.
     *
     * @param item the item
     */
    public void setItem(Item item) {
        this.item = item;
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
}

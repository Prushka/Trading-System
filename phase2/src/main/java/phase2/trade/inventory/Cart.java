package phase2.trade.inventory;

import javax.persistence.Entity;

@Entity
public class Cart extends ItemList {
    @Override
    public InventoryType getInventoryType() {
        return InventoryType.CART;
    }
}

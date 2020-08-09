package phase2.trade.inventory;

import javax.persistence.Entity;

@Entity
public class Cart extends ItemList {
    @Override
    public ItemListType getInventoryType() {
        return ItemListType.CART;
    }
}

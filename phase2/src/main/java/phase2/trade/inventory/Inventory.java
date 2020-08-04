package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.item.Ownership;

import javax.persistence.Entity;

@Entity
public class Inventory extends ItemList {
    @Override
    public InventoryType getInventoryType() {
        return InventoryType.INVENTORY;
    }

    @Override
    public void addItem(Item item) {
        item.setOwnership(Ownership.TO_BE_REVIEWED);
        super.addItem(item);
    }
}

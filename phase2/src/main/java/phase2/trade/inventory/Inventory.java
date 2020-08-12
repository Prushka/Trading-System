package phase2.trade.inventory;

import phase2.trade.item.Item;
import phase2.trade.item.Ownership;

import javax.persistence.Entity;

@Entity
public class Inventory extends ItemList {

    @Override
    public ItemListType getItemListType() {
        return ItemListType.INVENTORY;
    }

    public void removeItem(Item item){
        super.removeItem(item);
    }
}

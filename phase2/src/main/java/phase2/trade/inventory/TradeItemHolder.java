package phase2.trade.inventory;

import javax.persistence.Entity;

@Entity
public class TradeItemHolder extends ItemList {

    @Override
    public InventoryType getInventoryType() {
        return InventoryType.TRADE_ITEM_HOLDER;
    }
}

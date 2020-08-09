package phase2.trade.inventory;

import javax.persistence.Entity;

@Entity
public class TradeItemHolder extends ItemList {

    @Override
    public ItemListType getInventoryType() {
        return ItemListType.TRADE_ITEM_HOLDER;
    }
}

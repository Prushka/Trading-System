package phase2.trade.inventory;

import javax.persistence.Entity;

@Entity
public class TradeItemHolder extends ItemList {

    @Override
    public ItemListType getItemListType() {
        return ItemListType.TRADE_ITEM_HOLDER;
    }
}

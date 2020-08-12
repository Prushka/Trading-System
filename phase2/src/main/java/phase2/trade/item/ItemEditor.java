package phase2.trade.item;

import phase2.trade.callback.StatusCallback;
import phase2.trade.callback.StatusFailed;

import java.util.List;

public class ItemEditor {

    private final List<Item> items;

    public ItemEditor(List<Item> items){
        this.items = items;
    }

    public void alterWillingness(Willingness willingness, StatusCallback statusCallback){
        for(Item item:items){
            if(item.getOwnership()!=Ownership.OWNER) {
                statusCallback.call(new StatusFailed("You do not own this product yet!"));
            }
        }
        items.forEach(item -> item.setWillingness(willingness));
    }

    public List<Item> getItems() {
        return items;
    }
}

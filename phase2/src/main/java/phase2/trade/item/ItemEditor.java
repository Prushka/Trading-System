package phase2.trade.item;

import java.util.List;

public class ItemEditor {

    private final List<Item> items;

    public ItemEditor(List<Item> items){
        this.items = items;
    }

    public void alterWillingness(Willingness willingness){
        items.forEach(item -> item.setWillingness(willingness));
    }

    public List<Item> getItems() {
        return items;
    }
}

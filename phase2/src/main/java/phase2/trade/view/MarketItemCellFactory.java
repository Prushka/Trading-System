package phase2.trade.view;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import phase2.trade.item.Item;


public class MarketItemCellFactory implements Callback<ListView<Item>, ListCell<Item>> {

    @Override
    public ListCell<Item> call(ListView<Item> param) {
        return new MarketItemCell();
    }
}
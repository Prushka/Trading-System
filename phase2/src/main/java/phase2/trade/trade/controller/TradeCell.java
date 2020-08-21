package phase2.trade.trade.controller;

import com.jfoenix.controls.JFXListCell;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import phase2.trade.itemlist.ItemList;
import phase2.trade.trade.Trade;
import phase2.trade.trade.TradeOrder;

import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

/**
 * The Trade cell.
 *
 * @author Dan Lyu
 */
public class TradeCell extends JFXListCell<Trade> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private Pane generateTradePreview(Trade trade) {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);

        hBox.getStyleClass().add("market-trade-cell");
        hBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Set<Long> users = new HashSet<>();

        for (TradeOrder tradeOrder : trade.getOrders()) {
            HBox subHBox = new HBox(20);
            subHBox.setAlignment(Pos.CENTER_RIGHT);
            Label state = new Label(tradeOrder.getOrderState().name() + " ");
            state.getStyleClass().addAll("trade-state");
            Label outer = new Label(String.format("%s (%s items) <-> %s (%s items)",
                    tradeOrder.getLeftUser().getName(),
                    tradeOrder.getLeftBundle().getTradeItemHolder().size(), tradeOrder.getRightUser().getName(),
                    tradeOrder.getRightBundle().getTradeItemHolder().size()
            ));
            outer.getStyleClass().addAll("trade-detail");
            Label items = new Label(String.format("%s <-> %s",
                    getTradeItemHolderRepresentation(tradeOrder.getLeftBundle().getTradeItemHolder()),
                    getTradeItemHolderRepresentation(tradeOrder.getRightBundle().getTradeItemHolder())
            ));

            items.getStyleClass().addAll("trade-item-detail");

            users.add(tradeOrder.getLeftUser().getUid());
            users.add(tradeOrder.getRightUser().getUid());
            subHBox.getChildren().addAll(outer, state);
            rightVBox.getChildren().addAll(subHBox, items);
        }

        Label idLabel = new Label("Trade: " + trade.getUid());
        idLabel.getStyleClass().addAll("trade-uid");
        Label tradePlacedTime = new Label("Placed at: " + trade.getLocalDateTime().format(formatter));
        tradePlacedTime.getStyleClass().addAll("trade-placed-time");

        Label involvedUsers = new Label(users.size() + " Users Involved");
        involvedUsers.getStyleClass().addAll("trade-users-involved");

        leftVBox.getChildren().addAll(idLabel, tradePlacedTime, involvedUsers);

        hBox.getChildren().addAll(leftVBox, region, rightVBox);
        return hBox;
    }

    private String getTradeItemHolderRepresentation(ItemList tradeItemHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        tradeItemHolder.getSetOfItems().forEach(item -> {
            stringBuilder.append(item.getName()).append(", ");
        });
        String r = stringBuilder.toString();
        if (tradeItemHolder.size() == 0) {
            return "nothing";
        }
        return r.substring(0, r.length() - 2);
    }

    @Override
    protected void updateItem(Trade trade, boolean empty) {
        super.updateItem(trade, empty);
        if (empty) {
            setGraphic(null);
        } else {
            setGraphic(generateTradePreview(trade));
            setText("");
        }
    }
}

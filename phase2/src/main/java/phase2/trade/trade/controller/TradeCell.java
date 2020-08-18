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

public class TradeCell extends JFXListCell<Trade> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TradeCell() {
    }

    private Pane generateTradePreview(Trade trade) {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 20, 15, 20));
        VBox leftVBox = new VBox(5);
        VBox rightVBox = new VBox(5);

        hBox.setAlignment(Pos.CENTER_LEFT);
        leftVBox.setAlignment(Pos.CENTER_LEFT);
        rightVBox.setAlignment(Pos.CENTER_RIGHT);

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Set<Long> users = new HashSet<>();

        for (TradeOrder tradeOrder : trade.getOrders()) {

            String userRepresentation =
                    String.format("%s [ %s items <%s> ] <-> %s [ %s items <%s> ]",
                            tradeOrder.getLeftUser().getName(),
                            tradeOrder.getLeftBundle().getTradeItemHolder().size(),
                            getTradeItemHolderRepresentation(tradeOrder.getLeftBundle().getTradeItemHolder()),
                            tradeOrder.getRightUser().getName(), tradeOrder.getRightBundle().getTradeItemHolder().size(),
                            getTradeItemHolderRepresentation(tradeOrder.getRightBundle().getTradeItemHolder())
                    );
            users.add(tradeOrder.getLeftUser().getUid());
            users.add(tradeOrder.getRightUser().getUid());
            rightVBox.getChildren().addAll(new Label(userRepresentation));
        }

        Label idLabel = new Label("Trade: " + trade.getUid());
        Label tradePlacedTime = new Label("Placed at: " + trade.getLocalDateTime().format(formatter));

        Label involvedUsers = new Label(users.size() + " Users Involved");

        leftVBox.getChildren().addAll(idLabel, tradePlacedTime, involvedUsers);

        hBox.getChildren().addAll(leftVBox, region, rightVBox);
        hBox.getStyleClass().add("market-trade-cell");
        return hBox;
    }

    private String getTradeItemHolderRepresentation(ItemList tradeItemHolder) {
        StringBuilder stringBuilder = new StringBuilder();
        tradeItemHolder.getSetOfItems().forEach(item -> {
            stringBuilder.append(item.getName()).append(", ");
        });
        String r = stringBuilder.toString();
        if (tradeItemHolder.size() == 0) {
            return r;
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

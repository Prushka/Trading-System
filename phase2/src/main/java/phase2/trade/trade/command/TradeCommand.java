package phase2.trade.trade.command;

import phase2.trade.command.Command;

import javax.persistence.Entity;

@Entity
public abstract class TradeCommand<T> extends Command<T> {
    transient Long tradeId;

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
    }

}

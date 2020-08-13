package phase2.trade.trade;

import javax.persistence.*;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    private Order order;

    private TradeState tradeState;

    private boolean isPermanent;

    public Trade() {}

    public Long getUid() { return uid; }

    void setUid(Long uid) { this.uid = uid; }

    Order getOrder(){ return order; }

    void setOrder(Order order){ this.order = order;}

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public boolean getIsPermanent() {
        return isPermanent;
    }

    public void setIsPermanent(boolean permenant) {
        isPermanent = permenant;
    }
}
package phase2.trade.trade;

import javax.persistence.*;

@Entity
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne(cascade = CascadeType.ALL)
    private TradeOrder myOrder;

    private TradeState tradeState;

    private boolean isPermanent;

    public Trade() {}

    public Long getUid() { return uid; }

    void setUid(Long uid) { this.uid = uid; }

    TradeOrder getOrder(){ return myOrder; }

    void setOrder(TradeOrder order){ this.myOrder = order;}

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
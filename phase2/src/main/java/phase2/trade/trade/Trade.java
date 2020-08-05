package phase2.trade.trade;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToOne
    private Order order;

    private TradeState tradeState;

    public Trade() {}

    Long getUid() { return uid; }

    void setUid(Long uid) { this.uid = uid; }

    Order getOrder(){ return order; }

    void setOrder(Order order){ this.order = order;}

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    abstract Tradable getStrategy();

    abstract void setStrategy(Tradable newStrategy);
}
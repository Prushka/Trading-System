package phase2.trade.trade;

import phase2.trade.inventory.TradeItemHolder;
import phase2.trade.user.User;

import javax.persistence.*;

@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne
    private User initiator;

    @ManyToOne
    private User target;

    @OneToOne
    private TradeItemHolder tradeItemHolder;

}

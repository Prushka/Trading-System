package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.RegularUser;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TradeOrder {

    private Long uid;

    private List<UserOrderBundle> traders;

    private LocalDateTime dateAndTime;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<UserOrderBundle> getTraders() {
        return traders;
    }

    public void setTraders(List<UserOrderBundle> traders) {
        this.traders = traders;
    }

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    @Transient
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        for (UserOrderBundle user : traders) {
            users.add(user.getUser());
        }
        return users;
    }

    public boolean borrowed(User currUser) {
        for (UserOrderBundle user : traders) {
            if (user.getUser().equals(currUser) && !user.getTradeItemHolder().getSetOfItems().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean lent(User currUser) {
        for (UserOrderBundle user : traders) {
            if (!user.getUser().equals(currUser)) {
                for (Item item : user.getTradeItemHolder().getSetOfItems()) {
                    if (((RegularUser) currUser).getInventory().getSetOfItems().contains(item)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

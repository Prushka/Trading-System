package phase2.trade.trade;

import phase2.trade.item.Item;
import phase2.trade.user.PersonalUser;
import phase2.trade.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany
    private List<UserOrderBundle> traders;

    private LocalDateTime dateAndTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<UserOrderBundle> getTraders(){ return traders;}

    public void setTraders(List<UserOrderBundle> traders){ this.traders = traders;}

    public LocalDateTime getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(LocalDateTime dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public List<User> getUsers(){
        List<User> users = new ArrayList<>();
        for (UserOrderBundle user: traders){
            users.add(user.getUser());
        }
        return users;
    }

    public boolean borrowed(User currUser){
        for (UserOrderBundle user: traders){
            if (user.getUser().equals(currUser) && !user.getTradeItemHolder().getListOfItems().isEmpty()){
                return true;
            }
        }
        return false;
    }

    public  boolean lent(User currUser){
        for (UserOrderBundle user: traders){
            if (!user.getUser().equals(currUser)){
                for (Item item: user.getTradeItemHolder().getListOfItems()){
                    if (((PersonalUser) currUser).getInventory().getListOfItems().contains(item)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

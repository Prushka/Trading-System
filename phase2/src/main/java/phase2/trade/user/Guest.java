package phase2.trade.user;

public class Guest extends User {


    @Override
    public boolean isAdmin() {
        return false;
    }
}

package phase2.trade.trade;

import phase2.trade.user.Address;

import javax.persistence.Embedded;

public class MeetUpOrder extends Order {

    @Embedded
    private Address location;

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }
}

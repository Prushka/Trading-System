package phase2.trade.trade;

import phase2.trade.address.Address;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class MeetUpOrder extends Order {

    @OneToOne(cascade = CascadeType.ALL)
    private Address location;

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }
}

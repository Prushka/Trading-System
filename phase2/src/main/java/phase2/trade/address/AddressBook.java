package phase2.trade.address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();

    private int addressSelected = 0;

    public Address getSelectedAddress() {
        if (addresses.size() < addressSelected + 1 || addresses.get(addressSelected) == null) {
            return null;
        }
        return addresses.get(addressSelected);
    }

    public int getAddressSelected() {
        return addressSelected;
    }

    public void setSelectedAddress(Address address) {
        if (getSelectedAddress() == null) {
            addresses.add(address);
        } else {
            addresses.set(getAddressSelected(), address);
        }
    }

    public void setAddressSelected(int addressSelected) {
        this.addressSelected = addressSelected;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }
}

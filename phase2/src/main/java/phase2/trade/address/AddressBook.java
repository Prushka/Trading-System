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

    public Address cloneSelectedAddressWithoutDetail() {
        Address selected = getSelectedAddress();
        Address newAddress = new Address();
        if (selected == null) {
            return null;
        }
        newAddress.setCountry(selected.getCountry());
        newAddress.setCity(selected.getCity());
        newAddress.setTerritory(selected.getTerritory());
        return newAddress;
    }

    public void setSelectedAddress(Address address) {
        if (getSelectedAddress() == null) {
            addresses.add(address);
        } else {
            addresses.set(addressSelected, address);
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

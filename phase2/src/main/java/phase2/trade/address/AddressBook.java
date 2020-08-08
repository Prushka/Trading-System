package phase2.trade.address;

import javax.persistence.*;
import java.util.List;


@Entity
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @OneToMany
    private List<Address> addresses;

    private int addressSelected;

    public Address getSelectedAddress() {
        if(addresses.size() < addressSelected || addresses.get(addressSelected)==null){
            return null;
        }
        return addresses.get(addressSelected);
    }

    public int getAddressSelected() {
        return addressSelected;
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
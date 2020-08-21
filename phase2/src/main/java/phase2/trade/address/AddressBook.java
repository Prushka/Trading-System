package phase2.trade.address;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The AddressBook to be owned by a {@link phase2.trade.user.User}.
 *
 * @author Dan Lyu
 */
@Entity
public class AddressBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<Address> addresses = new ArrayList<>();

    private int addressSelected = 0;

    /**
     * Gets selected address.
     *
     * @return the selected address
     */
    public Address getSelectedAddress() {
        if (addresses.size() < addressSelected + 1 || addresses.get(addressSelected) == null) {
            return null;
        }
        return addresses.get(addressSelected);
    }

    /**
     * Clone selected address without detail address and return the new Address.
     *
     * @return the address
     */
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

    /**
     * Sets selected address.
     *
     * @param address the address
     */
    public void setSelectedAddress(Address address) {
        if (getSelectedAddress() == null) {
            addresses.add(address);
        } else {
            addresses.set(addressSelected, address);
        }
    }

    /**
     * Sets which index the selected Address should be.
     *
     * @param addressSelected the address selected
     */
    public void setAddressSelected(int addressSelected) {
        this.addressSelected = addressSelected;
    }

    /**
     * Gets the Id of this AddressBook.
     *
     * @return the uid
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the Id of this AddressBook.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

}

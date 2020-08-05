package phase2.trade.user;

import javax.persistence.Embeddable;

@Embeddable
public class Address {
    private String country;
    private String city;
    private String street;
    private  int streetNumber;

    public Address(String country, String city, String street, int streetNumber){
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }
}

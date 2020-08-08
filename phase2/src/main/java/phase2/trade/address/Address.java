package phase2.trade.address;

import javax.persistence.*;

@Entity
public class Address {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    private String country;
    private String city;
    private String street;
    private String streetNumber;

    public Address(String country, String city, String street, String streetNumber){
        this.country = country;
        this.city = city;
        this.street = street;
        this.streetNumber = streetNumber;
    }

    public Address(String country, String city){
        this.country = country;
        this.city = city;
    }

    public Address() {

    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }
}